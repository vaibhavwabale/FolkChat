package in.icomputercoding.folkchat.Model;



public class Comment {
    private String profile;
    private long commentId;
    private String comment;

    public Comment() {
    }

    public Comment(String profile, long commentId, String comment) {
        this.profile = profile;
        this.commentId = commentId;
        this.comment = comment;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public long getCommentId() {
        return commentId;
    }

    public void setCommentId(long commentId) {
        this.commentId = commentId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
