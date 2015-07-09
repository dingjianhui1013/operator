package cn.topca.security.bc.cms;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import org.bouncycastle.cms.CMSAttributeTableGenerator;
import org.bouncycastle.cms.RecipientInfoGenerator;

import cn.topca.security.bc.asn1.gm.GMObjectIdentifiers;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * 扩展BouncyCastle用于支持国产算法
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-04-29 14:03
 * @see org.bouncycastle.cms.CMSEnvelopedGenerator
 */
public class CMSEnvelopedGenerator extends org.bouncycastle.cms.CMSEnvelopedGenerator {

    public static final String SM4 = GMObjectIdentifiers.sm4.getId();

    final List oldRecipientInfoGenerators = new ArrayList();
    final List recipientInfoGenerators = new ArrayList();

    protected CMSAttributeTableGenerator unprotectedAttributeGenerator = null;

    final SecureRandom rand;

    public CMSEnvelopedGenerator() {
        this(new SecureRandom());
    }

    public CMSEnvelopedGenerator(
            SecureRandom rand) {
        super(rand);
        this.rand = rand;
    }

    @Override
    public void setUnprotectedAttributeGenerator(CMSAttributeTableGenerator unprotectedAttributeGenerator) {
        super.setUnprotectedAttributeGenerator(unprotectedAttributeGenerator);
        this.unprotectedAttributeGenerator = unprotectedAttributeGenerator;
    }

    @Override
    public void addRecipientInfoGenerator(RecipientInfoGenerator recipientGenerator) {
        super.addRecipientInfoGenerator(recipientGenerator);
        recipientInfoGenerators.add(recipientGenerator);
    }

}