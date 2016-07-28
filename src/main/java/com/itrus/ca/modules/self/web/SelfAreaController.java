package com.itrus.ca.modules.self.web;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.self.entity.SelfArea;
import com.itrus.ca.modules.self.service.SelfAreaService;

/**
 * 申请人Controller
 * 
 * @author HuHao
 * @version 2016-02-18
 */
@Controller
@RequestMapping(value = "${adminPath}/selfArea")
public class SelfAreaController extends BaseController {

    @Autowired
    private SelfAreaService selfAreaService;

    @ModelAttribute
    public SelfArea get(@RequestParam(required = false) Long id) {
        if (id != null) {
            return selfAreaService.get(id);
        } else {
            return new SelfArea();
        }
    }

    /**
     * 找到税务局下的市（州）
     * 
     * @return
     */
    @ResponseBody
    @RequestMapping("findAllPrivince")
    public String findAllPrivince() {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            List<SelfArea> list = selfAreaService.getProvinceJsonData();
            if (list.size() > 0) {
                for (SelfArea selfArea : list) {
                    JSONObject json = new JSONObject();
                    json.put("area_id", selfArea.getAreaId());
                    json.put("area_name", selfArea.getAreaName());
                    jsonArray.put(json);
                }
                jsonObject.put("list", jsonArray);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    /**
     * 根据地级市
     * 
     * @return
     */
    @ResponseBody
    @RequestMapping("findLower")
    public String findLower(String higher) {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            List<SelfArea> list = selfAreaService.getCityJsonData(higher);
            for (SelfArea selfArea : list) {
                JSONObject json = new JSONObject();
                json.put("area_id", selfArea.getAreaId());
                json.put("area_name", selfArea.getAreaName());
                json.put("parent_id", selfArea.getParentId());
                jsonArray.put(json);
            }
            jsonObject.put("list", jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    /**
     * 得到参保区县
     * 
     * @return
     */
    @ResponseBody
    @RequestMapping("SecurityAddress")
    public String getSecurityAddress() {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            List<SelfArea> list = selfAreaService.getSecurityAddress();
            for (SelfArea selfArea : list) {
                JSONObject json = new JSONObject();
                json.put("area_id", selfArea.getAreaId());
                json.put("area_name", selfArea.getAreaName());
                json.put("parent_id", selfArea.getParentId());
                jsonArray.put(json);
            }
            jsonObject.put("list", jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
