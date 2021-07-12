package com.example.bears.Model;


// Welcome7.java
public class stationByPos {
    public static class Welcome7 {
        private ServiceResult serviceResult;

        public ServiceResult getServiceResult() {
            return serviceResult;
        }

        public void setServiceResult(ServiceResult value) {
            this.serviceResult = value;
        }
    }
    // ServiceResult.java


    public static class ServiceResult {
        private String comMsgHeader;
        private MsgHeader msgHeader;
        private MsgBody msgBody;

        public ServiceResult(String comMsgHeader, MsgHeader msgHeader, MsgBody msgBody) {
            this.comMsgHeader = comMsgHeader;
            this.msgHeader = msgHeader;
            this.msgBody = msgBody;
        }

        public String getCOMMsgHeader() { return comMsgHeader; }

        public MsgHeader getMsgHeader() { return msgHeader; }

        public MsgBody getMsgBody() { return msgBody; }
    }

// MsgBody.java

    public static class MsgBody {
        private ItemList[] itemList;

        public ItemList[] getItemList() { return itemList; }
    }

    // ItemList.java
    public static class ItemList {
        private String arsID;
        private String dist;
        private String gpsX;
        private String gpsY;
        private String posX;
        private String posY;
        private String stationID;
        private String stationNm;
        private String stationTp;

        public String getArsID() { return arsID; }

        public String getDist() { return dist; }

        public String getGpsX() { return gpsX; }

        public String getGpsY() { return gpsY; }

        public String getPosX() { return posX; }

        public String getPosY() { return posY; }

        public String getStationID() { return stationID; }

        public String getStationNm() { return stationNm; }

        public String getStationTp() { return stationTp; }
    }

    // MsgHeader.java
    public static class MsgHeader {
        private String headerCD;
        private String headerMsg;
        private String itemCount;

        public String getHeaderCD() { return headerCD; }

        public String getHeaderMsg() { return headerMsg; }

        public String getItemCount() { return itemCount; }
    }

}


