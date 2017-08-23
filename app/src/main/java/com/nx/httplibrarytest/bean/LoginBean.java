package com.nx.httplibrarytest.bean;

import java.io.Serializable;

/**
 * @类描述： TODO
 * @创建人：王成丞
 * @创建时间：2017/8/9 17:32
 */
public class LoginBean implements Serializable{


    /**code  0 msg  成功
     *
     * data : {"username":"18514592015","header_img":"","nick_name":"18514592015","token":"bd9fMcrUeCLhT2FzBz0QRSiDxSnXtoMrUO6EpWG8fPLuiKvB9RCq4Gg+ygDlwxIfY9D+pHeqxnFC5UQXQDmGO9LzR0bnJox2pi44t+WY0f8f","user_name":"185****2015","isselecttopic":"1"}
     * resCode : 0000
     * resMsg : 成功
     */

    private DataBean data;
    private String resCode;
    private String resMsg;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getResMsg() {
        return resMsg;
    }

    public void setResMsg(String resMsg) {
        this.resMsg = resMsg;
    }

    public static class DataBean implements Serializable{
        /**
         * username : 18514592015
         * header_img :
         * nick_name : 18514592015
         * token : bd9fMcrUeCLhT2FzBz0QRSiDxSnXtoMrUO6EpWG8fPLuiKvB9RCq4Gg+ygDlwxIfY9D+pHeqxnFC5UQXQDmGO9LzR0bnJox2pi44t+WY0f8f
         * user_name : 185****2015
         * isselecttopic : 1
         */

        private String username;
        private String header_img;
        private String nick_name;
        private String token;
        private String user_name;
        private String isselecttopic;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getHeader_img() {
            return header_img;
        }

        public void setHeader_img(String header_img) {
            this.header_img = header_img;
        }

        public String getNick_name() {
            return nick_name;
        }

        public void setNick_name(String nick_name) {
            this.nick_name = nick_name;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getIsselecttopic() {
            return isselecttopic;
        }

        public void setIsselecttopic(String isselecttopic) {
            this.isselecttopic = isselecttopic;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "username='" + username + '\'' +
                    ", header_img='" + header_img + '\'' +
                    ", nick_name='" + nick_name + '\'' +
                    ", token='" + token + '\'' +
                    ", user_name='" + user_name + '\'' +
                    ", isselecttopic='" + isselecttopic + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "LoginBean{" +
                "data=" + data +
                ", resCode='" + resCode + '\'' +
                ", resMsg='" + resMsg + '\'' +
                '}';
    }
}
