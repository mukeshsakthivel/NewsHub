package Management.journalism.model;


import org.springframework.web.multipart.MultipartFile;

public class NewsUploadForm {

    @Override
	public String toString() {
		return "NewsUploadForm [title=" + title + ", image=" + image + ", content=" + content + "]";
	}

	private String title;
    private MultipartFile image;
    private String content;

    // getters and setters

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}