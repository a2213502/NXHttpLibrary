package com.nx.httplibrarytest.bean;

import java.io.Serializable;

/**
 * @类描述： TODO
 * @创建人：王成丞
 * @创建时间：2017/9/15 14:02
 */
public class TestBean implements Serializable{


    /**
     * data : {"activityInfo":{"activityID":"1234","bannerUrl":"xxxxx","activityRuleUrl":"xxxxx","activityState":0,"time":"2017.8.10-2017-8.05","title":"歌名：消愁 歌名：消愁 歌名：消愁 歌名：消愁 歌名：消愁 歌名：消愁 歌名：消愁 歌名：消愁 歌名：消愁 歌名：消愁 歌名：消愁 歌名：消愁 ","content":"当你走进这欢乐场，背上所有的梦与想，各色的脸上各色的妆，没人记得你的模样，三巡酒过你在角落，固执的，唱着苦涩的歌，听他在喧嚣里被淹没，你拿起酒杯对自己说，一杯敬朝阳，一杯敬月光，唤醒我的向往，温柔了寒窗，于是可以不回头的逆风飞翔，不怕心头有雨，眼底有霜，一杯敬故乡，一杯敬远方，守着，的善良，催着我成长，所以南北的路从此不再漫长，灵魂不再无处安放，一杯敬明天，一杯敬过往，支撑我的身体，厚重了肩膀，虽然从不相信所谓山高水长，人生苦短何必念念不忘，一杯敬自由，一杯敬死亡，宽恕我的平凡，驱散了迷惘，好吧天亮之后总是潦草离场，清醒的人最荒唐，好吧天亮之后总是潦草离场，清醒的人最荒唐"},"voteInfo":{"winer":"0","prosNumbers":"1600","consNumbers":"400","prosPercentage":"80%","consPercentage":"20%","prosPoint":"一杯敬自由,一杯敬死亡！！！","consPoint":"反方认为：不喝酒"},"totalScore":"1000"}
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

    public static class DataBean implements Serializable {
        /**
         * activityInfo : {"activityID":"1234","bannerUrl":"xxxxx","activityRuleUrl":"xxxxx","activityState":0,"time":"2017.8.10-2017-8.05","title":"歌名：消愁 歌名：消愁 歌名：消愁 歌名：消愁 歌名：消愁 歌名：消愁 歌名：消愁 歌名：消愁 歌名：消愁 歌名：消愁 歌名：消愁 歌名：消愁 ","content":"当你走进这欢乐场，背上所有的梦与想，各色的脸上各色的妆，没人记得你的模样，三巡酒过你在角落，固执的，唱着苦涩的歌，听他在喧嚣里被淹没，你拿起酒杯对自己说，一杯敬朝阳，一杯敬月光，唤醒我的向往，温柔了寒窗，于是可以不回头的逆风飞翔，不怕心头有雨，眼底有霜，一杯敬故乡，一杯敬远方，守着，的善良，催着我成长，所以南北的路从此不再漫长，灵魂不再无处安放，一杯敬明天，一杯敬过往，支撑我的身体，厚重了肩膀，虽然从不相信所谓山高水长，人生苦短何必念念不忘，一杯敬自由，一杯敬死亡，宽恕我的平凡，驱散了迷惘，好吧天亮之后总是潦草离场，清醒的人最荒唐，好吧天亮之后总是潦草离场，清醒的人最荒唐"}
         * voteInfo : {"winer":"0","prosNumbers":"1600","consNumbers":"400","prosPercentage":"80%","consPercentage":"20%","prosPoint":"一杯敬自由,一杯敬死亡！！！","consPoint":"反方认为：不喝酒"}
         * totalScore : 1000
         */

        private ActivityInfoBean activityInfo;
        private VoteInfoBean voteInfo;
        private String totalScore;

        public ActivityInfoBean getActivityInfo() {
            return activityInfo;
        }

        public void setActivityInfo(ActivityInfoBean activityInfo) {
            this.activityInfo = activityInfo;
        }

        public VoteInfoBean getVoteInfo() {
            return voteInfo;
        }

        public void setVoteInfo(VoteInfoBean voteInfo) {
            this.voteInfo = voteInfo;
        }

        public String getTotalScore() {
            return totalScore;
        }

        public void setTotalScore(String totalScore) {
            this.totalScore = totalScore;
        }

        public static class ActivityInfoBean {
            /**
             * activityID : 1234
             * bannerUrl : xxxxx
             * activityRuleUrl : xxxxx
             * activityState : 0
             * time : 2017.8.10-2017-8.05
             * title : 歌名：消愁 歌名：消愁 歌名：消愁 歌名：消愁 歌名：消愁 歌名：消愁 歌名：消愁 歌名：消愁 歌名：消愁 歌名：消愁 歌名：消愁 歌名：消愁
             * content : 当你走进这欢乐场，背上所有的梦与想，各色的脸上各色的妆，没人记得你的模样，三巡酒过你在角落，固执的，唱着苦涩的歌，听他在喧嚣里被淹没，你拿起酒杯对自己说，一杯敬朝阳，一杯敬月光，唤醒我的向往，温柔了寒窗，于是可以不回头的逆风飞翔，不怕心头有雨，眼底有霜，一杯敬故乡，一杯敬远方，守着，的善良，催着我成长，所以南北的路从此不再漫长，灵魂不再无处安放，一杯敬明天，一杯敬过往，支撑我的身体，厚重了肩膀，虽然从不相信所谓山高水长，人生苦短何必念念不忘，一杯敬自由，一杯敬死亡，宽恕我的平凡，驱散了迷惘，好吧天亮之后总是潦草离场，清醒的人最荒唐，好吧天亮之后总是潦草离场，清醒的人最荒唐
             */

            private String activityID;
            private String bannerUrl;
            private String activityRuleUrl;
            private int activityState;
            private String time;
            private String title;
            private String content;

            public String getActivityID() {
                return activityID;
            }

            public void setActivityID(String activityID) {
                this.activityID = activityID;
            }

            public String getBannerUrl() {
                return bannerUrl;
            }

            public void setBannerUrl(String bannerUrl) {
                this.bannerUrl = bannerUrl;
            }

            public String getActivityRuleUrl() {
                return activityRuleUrl;
            }

            public void setActivityRuleUrl(String activityRuleUrl) {
                this.activityRuleUrl = activityRuleUrl;
            }

            public int getActivityState() {
                return activityState;
            }

            public void setActivityState(int activityState) {
                this.activityState = activityState;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }
        }

        public static class VoteInfoBean {
            /**
             * winer : 0
             * prosNumbers : 1600
             * consNumbers : 400
             * prosPercentage : 80%
             * consPercentage : 20%
             * prosPoint : 一杯敬自由,一杯敬死亡！！！
             * consPoint : 反方认为：不喝酒
             */

            private String winer;
            private String prosNumbers;
            private String consNumbers;
            private String prosPercentage;
            private String consPercentage;
            private String prosPoint;
            private String consPoint;

            public String getWiner() {
                return winer;
            }

            public void setWiner(String winer) {
                this.winer = winer;
            }

            public String getProsNumbers() {
                return prosNumbers;
            }

            public void setProsNumbers(String prosNumbers) {
                this.prosNumbers = prosNumbers;
            }

            public String getConsNumbers() {
                return consNumbers;
            }

            public void setConsNumbers(String consNumbers) {
                this.consNumbers = consNumbers;
            }

            public String getProsPercentage() {
                return prosPercentage;
            }

            public void setProsPercentage(String prosPercentage) {
                this.prosPercentage = prosPercentage;
            }

            public String getConsPercentage() {
                return consPercentage;
            }

            public void setConsPercentage(String consPercentage) {
                this.consPercentage = consPercentage;
            }

            public String getProsPoint() {
                return prosPoint;
            }

            public void setProsPoint(String prosPoint) {
                this.prosPoint = prosPoint;
            }

            public String getConsPoint() {
                return consPoint;
            }

            public void setConsPoint(String consPoint) {
                this.consPoint = consPoint;
            }
        }
    }

    @Override
    public String toString() {
        return "TestBean{" +
                "data=" + data +
                ", resCode='" + resCode + '\'' +
                ", resMsg='" + resMsg + '\'' +
                '}';
    }
}
