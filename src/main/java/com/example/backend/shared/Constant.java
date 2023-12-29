package com.example.backend.shared;

import com.amazonaws.regions.Regions;
import com.example.backend.dtos.fshare.FshareLoginRequest;
import com.example.backend.models.User;

public class Constant {
    public static final String ADMIN_ID = "629ce7bb-2713-43be-a9bf-1538ff77218a";
    public static final User ADMIN = new User("629ce7bb-2713-43be-a9bf-1538ff77218a", "theanh2906@gmail.com", "theanh2906@gmail.com");
    public static final String[] CROSS_ORIGIN_ALLOW_LIST = new String[]{"http://localhost:8080", "http://localhost:4200"};

    public static class Firebase {
        public static final String API_URL = "https://useful-tools-api-default-rtdb.firebaseio.com";
        public static final String EVENTS_API = "https://useful-tools-api-default-rtdb.firebaseio.com/events.json";
    }

    public static class AWSConfig {
        public static final String SQS_ENDPOINT = "http://localhost:9324";
        public static final String AWS_CREDENTIAL_ACCESS_KEY = "AKIAR6KBL4DCX7ECZ62T";
        public static final String AWS_CREDENTIAL_SECRET_KEY = "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";
        public static final String AWS_IAM_ACCOUNT = "133817950405";
        public static final String AWS_ROOT_ACCOUNT = "theanh2906";
        public static final String AWS_SQS_API = "https://sqs.us-east-2.amazonaws.com/" + AWS_IAM_ACCOUNT + "/" + AWS_ROOT_ACCOUNT;
        public static final String AWS_SQS_REGION = Regions.US_EAST_2.getName();
        public static final String AWS_SQS_QUEUE_NAME = "theanh2906";
        public static final String AWS_S3_REGION = Regions.AP_SOUTHEAST_1.getName();
        public static final String AWS_S3_BUCKET_NAME = "theanh2906-bucket";
    }

    public static class Fshare {
        public static final String EMAIL = "theanh2906@outlook.com";
        public static final String APP_KEY = "dMnqMMZMUnN5YpvKENaEhdQQ5jxDqddt";
        public static final String USER_AGENT = "DevTools-U9DRRB";
        public static final String PASSWORD = "BenNa1402*";
        public static final String FSHARE_API_URL = "https://api.fshare.vn/api";
        public static final String FILE_FOLDER_INFO_URL = FSHARE_API_URL + "/fileops/list";
        public static final String LOGIN_URL = FSHARE_API_URL + "/user/login";
        public static final String LOGOUT_URL = FSHARE_API_URL + "/user/logout";
        public static final String DELETE_TOKEN_URL = "https://www.fshare.vn/account/delete-token";
        public static final String USER_INFO_URL = FSHARE_API_URL + "/user/get";
        public static final String UPLOAD_URL = FSHARE_API_URL + "/session/upload";
        public static final FshareLoginRequest LOGIN_REQUEST = new FshareLoginRequest(EMAIL, PASSWORD, APP_KEY);

        public enum FolderType {
            FOLDER("0"),
            FILE("1");

            FolderType(String value) {
                this.value = value;
            }

            public String getValue() {
                return value;
            }

            private final String value;
        }
    }

    public static class HeaderAttribute {
        public static final String AUTHORIZATION = "Authorization";
        public static final String CONTENT_TYPE = "Content-Type";
        public static final String ACCEPT = "Accept";
        public static final String USER_AGENT = "User-Agent";
        public static final String ACCEPT_LANGUAGE = "Accept-Language";
        public static final String ACCEPT_ENCODING = "Accept-Encoding";
        public static final String ACCEPT_CHARSET = "Accept-Charset";
        public static final String ACCEPT_DATETIME = "Accept-Datetime";
        public static final String ACCEPT_RANGES = "Accept-Ranges";
        public static final String ACCEPT_PATCH = "Accept-Patch";
        public static final String ACCEPT_POST = "Accept-Post";
        public static final String ACCEPT_PROPOSAL = "Accept-Proposal";
        public static final String ACCEPT_QUERY = "Accept-Query";
        public static final String ACCEPT_RDF = "Accept-RDF";
        public static final String ACCEPT_RSS = "Accept-RSS";
        public static final String ACCEPT_SCHEME = "Accept-Scheme";
        public static final String ACCEPT_SESSION = "Accept-Session";
        public static final String ACCEPT_SVG = "Accept-SVG";
        public static final String ACCEPT_TEXT = "Accept-Text";
        public static final String ACCEPT_XML = "Accept-XML";
        public static final String ACCEPT_XHTML = "Accept-XHTML";
        public static final String ACCEPT_XSL = "Accept-XSL";
        public static final String ACCEPT_X_JAVASCRIPT = "Accept-X-Javascript";
        public static final String ACCEPT_X_LINK = "Accept-X-Link";
        public static final String ACCEPT_X_STYLESHEET = "Accept-X-Stylesheet";
        public static final String ACCEPT_X_SVG = "Accept-X-SVG";
        public static final String ACCEPT_X_HTML = "Accept-X-HTML";
        public static final String ACCEPT_X_HTLM = "Accept-X-HTLM";
        public static final String SESSION_ID = "session_id";
    }
}
