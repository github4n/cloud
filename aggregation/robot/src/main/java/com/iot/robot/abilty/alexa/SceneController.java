package com.iot.robot.abilty.alexa;


public final class SceneController extends Alexa {

	private Boolean supportsDeactivation = false;
	private Boolean proactivelyReported = true;
	private SceneController() {
		super.setInterface(SceneController.class.getSimpleName());
	}
	public static SceneController getInstance() {
		return new SceneController();
	}
	public Boolean getSupportsDeactivation() {
		return supportsDeactivation;
	}
	public void setSupportsDeactivation(Boolean supportsDeactivation1) {
		this.supportsDeactivation = supportsDeactivation1;
	}
	public Boolean getProactivelyReported() {
		return proactivelyReported;
	}
	public void setProactivelyReported(Boolean proactivelyReported1) {
		this.proactivelyReported = proactivelyReported1;
	}
}
