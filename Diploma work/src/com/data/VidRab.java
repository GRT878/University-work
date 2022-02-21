package com.data;

public class VidRab {
	private String vidRab = null;
	
	public VidRab() {
		super();
	}

	public VidRab(String vidRab) {
		super();
		this.setVidRab(vidRab);
	}

	public String getVidRab() {
		return vidRab;
	}
	public void setVidRab(String vidRab) {
		this.vidRab = vidRab;
	}

	@Override
	public String toString() {
		return vidRab;
	}
}
