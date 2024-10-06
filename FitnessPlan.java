
public class FitnessPlan {
    private String planType;
    private int duration;  // minutes per week
    private String fitnessLevel;
    private String healthGoal;

    public FitnessPlan(String planType, int duration, String fitnessLevel, String healthGoal) {
        this.planType = planType;
        this.duration = duration;
        this.fitnessLevel = fitnessLevel;
        this.healthGoal = healthGoal;
    }

    public String getPlanType() {
        return planType;
    }

    public int getDuration() {
        return duration;
    }

    public String getFitnessLevel() {
        return fitnessLevel;
    }

    public String getHealthGoal() {
        return healthGoal;
    }
}
