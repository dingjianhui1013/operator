/**
 * 2010-6-17 下午12:13:11
 */
package com.itrus.ca.common.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

/**
 * @author bin_liu
 */
public class FileHelper {

	public static void copyFile(File inFile, File outFile) throws IOException {

		FileInputStream fi = null;
		FileOutputStream fo = null;
		FileChannel in = null;
		FileChannel out = null;
		try {

			fi = new FileInputStream(inFile);
			fo = new FileOutputStream(outFile);
			in = fi.getChannel();// 得到对应的文件通道
			out = fo.getChannel();// 得到对应的文件通道

			in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道
		} catch (IOException e) {
			e.printStackTrace();
			throw new IOException(e);
		} finally {
			try {
				fi.close();
				in.close();
				fo.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw new IOException(e);
			}
		}
	}

	/**
	 * 得到文件大小
	 * 
	 * @param file
	 * @param type
	 *            (0:K 1:M 2:G)
	 * @return String
	 * @throws IOException
	 */
	public static String getFileSize(String file, int type) throws IOException {

		String size = "0";

		switch (type) {
		case -1:
			size = getLongFileSize(file, type) + " Byte";
			break;
		case 0:
			size = getDoubleFileSize(file, type) + " KB";
			break;
		case 1:
			size = getDoubleFileSize(file, type) + " MB";
			break;
		case 2:
			size = getDoubleFileSize(file, type) + " GB";
			break;
		default:
			size = getDoubleFileSize(file, 1) + " M";
		}

		return size;
	}

	/**
	 * @param file
	 * @param type
	 *            (-1:byte 0:kb 1:mb 2:gb)
	 * @return long
	 * @throws IOException
	 */
	public static double getDoubleFileSize(String file, int type)
			throws IOException {
		File f = new File(file);
		double size = new Long(f.length()).doubleValue();

		switch (type) {
		case 0:
			size = size / 1024;
			break;
		case 1:
			size = size / 1024 / 1024;
			break;
		case 2:
			size = size / 1024 / 1024 / 1024;
			break;
		default:
			size = size / 1024 / 1024 / 1024;
		}

		return StringHelper.formartDecimalToDouble(size);
	}

	/**
	 * 得到文件大小
	 * 
	 * @param file
	 * @param type
	 *            (0:K 1:M 2:G)
	 * @return String
	 * @throws IOException
	 */
	public static Long getLongFileSize(String file, int type)
			throws IOException {
		File f = new File(file);

		return f.length();
	}

	/**
	 * 切割文件，不过重复行
	 * 
	 * @param sourceFile
	 * @param line
	 * @throws IOException
	 */
	public static void splitFileByLineFilter(String sourceFile, int line)
			throws IOException {
		splitFileByLine(sourceFile, line, false);
	}

	/**
	 * 切割文件，过滤重复行
	 * 
	 * @param sourceFile
	 * @param line
	 * @throws IOException
	 */
	public static void splitFileByLineNoFilter(String sourceFile, int line)
			throws IOException {
		splitFileByLine(sourceFile, line, true);
	}

	/**
	 * 按行数切割文件，指定多少行分一个文件
	 * 
	 * @param sourceFile
	 * @param line
	 * @param isFilter
	 *            是否过重复行
	 * @throws IOException
	 */
	public static void splitFileByLine(String sourceFile, int line,
			boolean isFilter) throws IOException {
		String[] temp = null;
		if (isFilter)
			temp = getLine(sourceFile, true);
		else
			temp = getLine(sourceFile, false);
		int flag = 0;

		Collection<String> l = new ArrayList<String>();
		int fileCount = 0;
		for (String e : temp) {
			l.add(e);
			if (flag % line == 0) {
				FileHelper.recordFetchToFile(sourceFile + "_" + fileCount
						+ ".tmp", l.toArray(new String[0]));
				fileCount++;
				l.clear();
			}
			flag++;
		}
		FileHelper.recordFetchToFile(sourceFile + "_" + fileCount + ".tmp",
				l.toArray(new String[0]));
		l.clear();
	}

	/**
	 * 按大小切割文件，生成在源文件的相同目录下
	 * 
	 * @param sourceFile
	 * @param size
	 *            (单位M)
	 * @throws IOException
	 */
	public static void splitFile(String sourceFile, int size)
			throws IOException {

		File file = new File(sourceFile);
		long c = file.length();
		long cSize = size * 1024 * 1024;
		// 小于需要分割的大小则不处理
		if (c <= size) {
			return;
		}
		// 切割多少个文件
		int count = (int) (c / cSize);
		if (c % cSize > 0) {
			count++;
		}
		// System.out.println("原文件大小：" + c + " | mb: " + ( c / 1024 / 1024 ));
		// System.out.println("指定分割的文件大小：" + cSize + " | mb: "
		// + ( cSize / 1024 / 1024 ));
		long splitSize = c / count;
		// System.out.println("切割多少个文件: " + count);
		// System.out.println("每个文件大小：" + splitSize);

		splitFileByCount(sourceFile, count);

	}

	/**
	 * 按指定文件数分割文件
	 * 
	 * @param sourceFile
	 * @param count
	 * @throws IOException
	 */
	public static void splitFileByCount(String sourceFile, int count)
			throws IOException {
		// 定义输出路径，文件名
		String outPath = new File(sourceFile).getAbsolutePath();
		String outSuffix = getSuffix(sourceFile);

		RandomAccessFile raf = new RandomAccessFile(new File(sourceFile), "r");
		long length = raf.length();

		long theadMaxSize = length / count; // 每份的大小 1024 * 1000L;
		raf.close();

		long offset = 0L;
		for (int i = 0; i < count - 1; i++) // 这里不去处理最后一份
		{
			long fbegin = offset;
			long fend = (i + 1) * theadMaxSize;
			System.out.println("offset:" + offset + " | i:" + i + " | fbegin:"
					+ fbegin + " | fend:" + fend);
			offset = write(sourceFile, i, fbegin, fend);
		}

		if (length - offset > 0) // 将剩余的都写入最后一份
			write(sourceFile, count - 1, offset, length);
	}

	/**
	 * <p>
	 * 指定每份文件的范围写入不同文件
	 * </p>
	 * 
	 * @param file
	 *            源文件
	 * @param index
	 *            文件顺序标识
	 * @param begin
	 *            开始指针位置
	 * @param end
	 *            结束指针位置
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	private static long write(String file, int index, long begin, long end)
			throws IOException {
		RandomAccessFile in = new RandomAccessFile(new File(file), "r");
		RandomAccessFile out = new RandomAccessFile(new File(file + "_" + index
				+ ".tmp"), "rw");
		byte[] b = new byte[1024];
		int n = 0;
		in.seek(begin);// 从指定位置读取

		while (in.getFilePointer() <= end && (n = in.read(b)) != -1) {
			out.write(b, 0, n);
		}
		long endPointer = in.getFilePointer();
		in.close();
		out.close();
		return endPointer;
	}

	/**
	 * <p>
	 * 合并文件
	 * </p>
	 * 
	 * @param file
	 *            指定合并后的文件
	 * @param tempFiles
	 *            分割前的文件名
	 * @param tempCount
	 *            文件个数
	 * @throws Exception
	 */
	public static void merge(String file, String tempFiles, int tempCount)
			throws Exception {
		RandomAccessFile ok = new RandomAccessFile(new File(file), "rw");

		for (int i = 0; i < tempCount; i++) {
			RandomAccessFile read = new RandomAccessFile(new File(tempFiles
					+ "_" + i + ".tmp"), "r");
			byte[] b = new byte[1024];
			int n = 0;
			while ((n = read.read(b)) != -1) {
				ok.write(b, 0, n);
			}
			read.close();
		}
		ok.close();
	}

	/**
	 * 得到文件后缀
	 * 
	 * @param file
	 * @return String
	 * @throws IOException
	 */
	public static String getSuffix(String file) throws IOException {
		String suffix = "";
		File f = new File(file);
		if (f.getName().indexOf(".") < 0)
			return "";
		suffix = f.getName().substring(f.getName().indexOf(".") + 1);

		return suffix;
	}

	/**
	 * 读文件进一个BYTE[]
	 * 
	 * @param file
	 * @return byte[]
	 * @throws IOException
	 */
	public static byte[] getBytesFromFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);

		// Get the size of the file
		long length = file.length();

		// You cannot create an array using a long type.
		// It needs to be an int type.
		// Before converting to an int type, check
		// to ensure that file is not larger than Integer.MAX_VALUE.
		if (length > Integer.MAX_VALUE) {
			// File is too large
			throw new IOException("file too large");
		}

		// Create the byte array to hold the data
		byte[] bytes = new byte[(int) length];

		// Read in the bytes
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length
				&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}

		// Ensure all the bytes have been read in
		if (offset < bytes.length) {
			throw new IOException("Could not completely read file "
					+ file.getName());
		}

		// Close the input stream and return bytes
		is.close();
		return bytes;
	}

	/**
	 * 得到文件类型
	 * 
	 * @param file
	 * @return String
	 */
	public static String getFiletype(String file) throws IOException {
		if (file.indexOf(".") < 0)
			return "";

		return file.substring(file.lastIndexOf(".") + 1);
	}

	/**
	 * 返回指定文件切割好的每一段内容 注意每一行的分割符号必须一致,元素个数可以不一致
	 * 
	 * @param file
	 * @param split
	 * @return String[][] (int:序号,String[] : 所有内容)
	 */
	public static String[][] getFileBySplit(String file, String split)
			throws FileNotFoundException, IOException {
		int page = 0;
		String str;
		BufferedReader bfr = new BufferedReader(new FileReader(file));

		List list = new LinkedList();
		String temp = "";
		while ((str = bfr.readLine()) != null) {
			list.add(str);
			if (page == 0)
				temp = str;
			page++;
		}
		String[][] result = new String[page][temp.length()];

		for (int i = 0; i < page; i++) {
			result[i] = StringHelper.splitStr((String) list.get(i), split);
		}
		return result;
	}// end....

	/**
	 * 得到一个文本中的所有行
	 * 
	 * @param file
	 * @return String[]
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static String[] getLine(String file) throws FileNotFoundException,
			IOException {
		return getLine(file, false);
	}

	/**
	 * 得到一个文本中的所有行
	 * 
	 * @param file
	 * @return String[]
	 * @throws IOException
	 */
	public static String[] getLineFilter(String file) throws IOException {
		return getLine(file, true);
	}

	public static String[] getLine(String file, boolean isFilter)
			throws IOException {

		int page = 0;
		String str;
		BufferedReader bfr = new BufferedReader(new FileReader(file));

		Collection<String> result = null;
		if (isFilter)
			result = new HashSet<String>();
		else
			result = new Vector<String>();
		while ((str = bfr.readLine()) != null) {
			result.add(str);
			page++;
		}

		bfr.close();
		return (String[]) result.toArray(new String[0]);
	}

	public static List<String> getLineToList(String file) throws IOException {
		String str;
		BufferedReader bfr = new BufferedReader(new FileReader(file));
		int page = 0;
		List<String> result = new Vector<String>();
		while ((str = bfr.readLine()) != null) {
			result.add(str);
			page++;
		}
		bfr.close();
		return result;
	}

	public static String getContent(String file) throws FileNotFoundException,
			IOException {

		BufferedReader bfr = new BufferedReader(new FileReader(file));
		String str;
		StringBuffer res = new StringBuffer();
		while ((str = bfr.readLine()) != null) {
			res.append(str).append("\n");
		}

		bfr.close();
		return res.toString();
	}

	/**
	 * 指定文件下，按指定字符获取内容
	 * 
	 * @param file
	 * @param character
	 *            指定字符
	 * @return String[]
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static String[] getLine(String file, String character)
			throws FileNotFoundException, IOException {
		int page = 0;
		String str;
		BufferedReader bfr = new BufferedReader(new FileReader(file));

		List result = new Vector();
		while ((str = bfr.readLine()) != null) {
			result.add(new String(str.getBytes(), character));
			page++;
		}
		bfr.close();
		return (String[]) result.toArray(new String[0]);
	}

	/**
	 * 批量写文件
	 * 
	 * @param file
	 * @param content
	 * @return int
	 */
	public static int recordFetchToFile(String file, String[] content) {
		File f = new File(file);
		try {
			FileWriter fw = new FileWriter(f, true);
			BufferedWriter bw = new BufferedWriter(fw);
			for (int i = 0; i < content.length; i++) {
				bw.write(content[i]);
				bw.newLine();
			}
			bw.close();
		} catch (IOException ex) {
			ex.printStackTrace();
			System.out.println("不能写入文件,错误原因: " + ex.getMessage());
			return 2;
		} catch (Throwable ex) {
			ex.printStackTrace();
			System.out.println("不能写入文件,错误原因: " + ex.getMessage());
			return 9;
		}
		return 0;
	}

	/**
	 * 批量写文件(可选择是否可追加)
	 * 
	 * @param file
	 * @param content
	 * @param append
	 * @return int
	 */
	public static int recordFetchToFile(String file, String[] content,
			boolean append) {
		File f = new File(file);
		try {
			FileWriter fw = new FileWriter(f, append);
			BufferedWriter bw = new BufferedWriter(fw);
			for (int i = 0; i < content.length; i++) {
				bw.write(content[i]);
				bw.newLine();
			}
			bw.close();
		} catch (IOException ex) {
			ex.printStackTrace();
			System.out.println("不能写入文件,错误原因: " + ex.getMessage());
			return 2;
		} catch (Throwable ex) {
			ex.printStackTrace();
			System.out.println("不能写入文件,错误原因: " + ex.getMessage());
			return 9;
		}
		return 0;
	}

	/**
	 * 追加一行进指定文本
	 * 
	 * @param file
	 *            (文件位置，要包含文件名)
	 * @param content
	 *            (内容，写完后会自动换行)
	 * @param append
	 *            (是否追加记录)
	 * @return int (0是正常,2是IO错误,9是未知错误)
	 */
	public static int recordLineToFile(String file, String content,
			boolean append) {
		File f = new File(file);
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			fw = new FileWriter(f, append);
			bw = new BufferedWriter(fw);
			bw.write(content);
			bw.newLine();
			bw.close();
		} catch (IOException ex) {
			ex.printStackTrace();
			System.out.println("不能写入文件,错误原因: " + ex.getMessage());
			return 2;
		} catch (Throwable ex) {
			ex.printStackTrace();
			System.out.println("不能写入文件,错误原因: " + ex.getMessage());
			return 9;
		} finally {
			try {
				if (fw != null)
					fw.close();
				if (bw != null)
					bw.close();
			} catch (IOException e) {
				System.out.println(StringHelper.getStackInfo(e));
			}
		}
		return 0;
	}

	/**
	 * 可设置保存文件的字符编码(由于JDK BUG，在WIN OS下文件名或内容没有中文时会出现编码依然是ANSI的情况)
	 * 
	 * @param file
	 * @param content
	 * @param character
	 * @param append
	 * @throws IOException
	 */
	public static void recordLineToFile(String file, String content,
			String character) throws IOException {
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;

		fos = new FileOutputStream(file);
		osw = new OutputStreamWriter(fos, character);
		osw.write(content);

		if (osw != null)
			osw.close();

		if (fos != null)
			fos.close();

	}

	// ToT
	public static File[] getAllFile(String path, List<File> fileList)
			throws IOException {
		if (fileList == null)
			fileList = new ArrayList<File>();
		File[] fList = getFilelist(path);
		for (File f : fList) {
			if (f.isDirectory()) {
				getAllFile(f.getAbsolutePath(), fileList);
				continue;
			} else
				fileList.add(f);
		}
		return (File[]) fileList.toArray(new File[0]);
	}

	/**
	 * 得到一个目录下的所有子目录
	 * 
	 * @param path
	 * @return File[]
	 * @throws IOException
	 */
	public static File[] getSubdirName(String path) throws IOException {
		List l = new Vector();
		File[] f = new File(path).listFiles();

		if (f == null)
			throw new IOException("Error path: " + path);

		for (int i = 0; i < f.length; i++) {
			if (f[i].isDirectory())
				l.add(f[i]);
		}
		return (File[]) l.toArray(new File[0]);
	}

	/**
	 * 得到一个目录下所有文件和目录
	 * 
	 * @param path
	 * @return String[]
	 * @throws IOException
	 */
	public static File[] getFilelist(String path) throws IOException {
		File[] f = new File(path).listFiles();
		List l = new Vector();

		if (f == null)
			throw new IOException("Error path: " + path);

		for (int i = 0; i < f.length; i++) {
			// if(!f[i].isDirectory())
			l.add(f[i]);
		}
		return (File[]) l.toArray(new File[0]);
	}

	/**
	 * 深度建立文件夹，如果中间哪个文件夹不存在则建立
	 * 
	 * @param path
	 */
	public static void mkdir(String path) {
		String[] dir = StringHelper.splitStr(path, "/");

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < dir.length; i++) {

			sb.append(dir[i] + "/");
			File f = new File(sb.toString());
			if (!f.exists()) {
				f.mkdir();
			}
		}
	}

	public static void clearDir(String dir) throws IOException {
		File[] f = getFilelist(dir);
		for (File e : f) {
			if (e.isDirectory())
				clearDir(e.getAbsolutePath());
			e.delete();
		}
	}

	public static void main(String[] args) throws Exception {
		// System.out.println(getFileSize("D:/temp/mobile/blackuser/black.txt",1));

		// System.out.println(new File("D:/temp/mobile/blackuser/black.txt")
		// .length() / 1000 / 1000);
		//
		// System.out.println(getSuffix("D:/temp/mobile/blackuser/black.txt"));
		//
		// splitFile("D:/temp/mobile/blackuser/black.txt",100);

		// merge("D:/temp/mobile/blackuser/black_1.txt","D:/temp/mobile/blackuser/black.txt",5);
		System.out.println("finish-----");
		// mkdir("d:/Temp/wenming/20010203");
		// File[] f1 = getSubdirName("e:/log");
		//
		// for(int i = 0;i < f1.length;i ++ ){
		// System.out.println(f1[i]);
		// }
		//
		// File[] f2 = getFilelist("e:/log");
		//
		// for(int i = 0;i < f2.length;i ++ ){
		// System.out.println(f2[i]);
		// System.out.println(getFiletype(f2[i].getName()));
		// }

		recordLineToFile("d:/temp_001.txt", "sdjkd", "UTF-8");

	}
}
