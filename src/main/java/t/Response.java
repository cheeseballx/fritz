package t;

public class Response {

    private String content;
    private int code;

    public Response(int code, String content){
        this.code = code;
        this.content = content;
    }

    public int getCode(){
        return this.code;
    }

    public String getContent(){
        return this.content;
    }
    
}