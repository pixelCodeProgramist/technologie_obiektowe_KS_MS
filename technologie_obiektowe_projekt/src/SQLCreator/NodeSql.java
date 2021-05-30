package SQLCreator;

public class NodeSql {
    private String header;
    private String body;

    public NodeSql() {
        this.header = "";
        this.body = "";
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setHeaderAndBody(String str) {
        String[] splittedStr = str.split("\n");
        for (int i = 0; i < splittedStr.length; i++) {
            if (i == 0) setHeader(splittedStr[0]);
            else {
                if (!body.equals(""))
                    setBody(getBody() + "\n" + splittedStr[i]);
                else setBody(splittedStr[i]);
            }
        }
    }

}
