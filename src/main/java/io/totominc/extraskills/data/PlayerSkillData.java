package io.totominc.extraskills.data;

public final class PlayerSkillData {
  private double experienceRequired;
  private double experience = 0;
  private double level = 1;

  public PlayerSkillData(double initialExperienceRequired) {
    this.experienceRequired = initialExperienceRequired;
  }

  public void setExperienceRequired(double amount) {
    this.experienceRequired = amount;
  }

  public double getExperienceRequired() {
    return this.experienceRequired;
  }

  public void setExperience(double amount) {
    this.experience = amount;
  }

  public void addExperience(double amount) {
    this.experience += amount;
  }

  public void removeExperience(double amount) {
    this.experience -= amount;
  }

  public double getExperience() {
    return this.experience;
  }

  public void setLevel(double level) {
    this.level = level;
  }

  public void addLevels(double level) {
    this.level += level;
  }

  public double getLevel() {
    return this.level;
  }
}
