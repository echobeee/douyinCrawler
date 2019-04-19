package douyinSpider;

public class VideoInfo {
	
	// video 链接
	String videoUrl;
	
	String aweme_id;
	
	// 评论数
	long comment_count;
	
	// 点赞数
	long digg_count;
	
	// share数
	long share_count;

	// 转发数
	long forward_count;
	
	public String getAweme_id() {
		return aweme_id;
	}

	public void setAweme_id(String aweme_id) {
		this.aweme_id = aweme_id;
	}

	public long getPlay_count() {
		return play_count;
	}

	public void setPlay_count(long play_count) {
		this.play_count = play_count;
	}

	long play_count;
	
	public VideoInfo() {
		// TODO Auto-generated constructor stub
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public long getComment_count() {
		return comment_count;
	}

	public void setComment_count(long comment_count) {
		this.comment_count = comment_count;
	}

	public long getDigg_count() {
		return digg_count;
	}

	public void setDigg_count(long digg_count) {
		this.digg_count = digg_count;
	}

	public long getShare_count() {
		return share_count;
	}

	public void setShare_count(long share_count) {
		this.share_count = share_count;
	}

	public long getForward_count() {
		return forward_count;
	}

	public void setForward_count(long forward_count) {
		this.forward_count = forward_count;
	}

	@Override
	public String toString() {
		return "VideoInfo [videoUrl=" + videoUrl + ", aweme_id=" + aweme_id + ", comment_count=" + comment_count
				+ ", digg_count=" + digg_count + ", share_count=" + share_count + ", forward_count=" + forward_count
				+ ", play_count=" + play_count + "]";
	}
   
	
	
}
