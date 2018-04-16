package com.thinkgem.jeesite.config;

import java.util.Arrays;
import java.util.Optional;

/**
 * Created by Administrator on 2017-05-24.
 */
public class EnumYzfUtil {
    //操所审核枚举
    public static enum YzfChangeType {
        //自主升级
        AUTO_UP_LEVEL(40),
        //用户转账
        TRANSFER_ACCOUNTS(41);


        private int nCode;

        private YzfChangeType(int _nCode) {
            this.nCode = _nCode;
        }

        @Override
        public String toString() {
            return String.valueOf(this.nCode);
        }
    }

}
