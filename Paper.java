public class Paper {
    private int id;
    private String title;
    private String author;
    private int year;
    private String domain;
    private int citations;
    private String keywords;
    private String url;

    public Paper(int id, String title, String author, int year,
                 String domain, int citations, String keywords, String url) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
        this.domain = domain;
        this.citations = citations;
        this.keywords = keywords;
        this.url = url;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getYear() { return year; }
    public String getDomain() { return domain; }
    public int getCitations() { return citations; }
    public String getKeywords() { return keywords; }
    public String getUrl() { return url; }

    public String searchableText() {
        return (title + " " + author + " " + domain + " " + keywords).toLowerCase();
    }

    public void display() {
        System.out.println("--------------------------------------");
        System.out.println("ID        : " + id);
        System.out.println("Title     : " + title);
        System.out.println("Author    : " + author);
        System.out.println("Year      : " + year);
        System.out.println("Domain    : " + domain);
        System.out.println("Citations : " + citations);
        System.out.println("Keywords  : " + keywords);
        System.out.println("Source    : " + url);
        System.out.println("--------------------------------------");
    }
}
