package kth.jjve.xfran.utils;

import kth.jjve.xfran.R;
import kth.jjve.xfran.adapters.AppCtx;
import kth.jjve.xfran.models.Workout;

import static android.text.InputType.TYPE_DATETIME_VARIATION_TIME;

public class ResultUtils {

    //sets the score type text depending on the type of workout
    public static String setScoreType(Workout workout) {
        String workoutType = workout.getType();
        switch (workoutType) {
            case "for-time":
                return AppCtx.getContext().getResources().getString(R.string.score_for_time);
            case "amrap":
                return AppCtx.getContext().getResources().getString(R.string.score_amrap);
            case "emom":
                return AppCtx.getContext().getResources().getString(R.string.score_emom);
            case "for-weight":
                return AppCtx.getContext().getResources().getString(R.string.score_for_weight);
            case "sets":
                return AppCtx.getContext().getResources().getString(R.string.score_sets);
            default:
                return AppCtx.getContext().getResources().getString(R.string.score_type);
        }
    }

    public static String setScoreTypeHint(Workout workout) {
        String workoutType = workout.getType();
        switch (workoutType) {
            case "for-time":
                return AppCtx.getContext().getResources().getString(R.string.score_for_time_hint);
            case "amrap":
                return AppCtx.getContext().getResources().getString(R.string.score_amrap_hint);
            case "emom":
                return AppCtx.getContext().getResources().getString(R.string.score_emom_hint);
            case "for-weight":
                return AppCtx.getContext().getResources().getString(R.string.score_for_weight_hint);
            case "sets":
                return AppCtx.getContext().getResources().getString(R.string.score_sets_hint);
            default:
                return AppCtx.getContext().getResources().getString(R.string.nohint);
        }
    }

    public static boolean isWrongScore(Workout workout, String score) {
        //Todo: finish this safety feature in case input info is crap
        String workoutType = workout.getType();
        switch (workoutType) {
            case "for-time":
                return false;
            case "amrap":
                return false;
            case "emom":
                return false;
            case "for-weight":
                return false;
            case "sets":
                return false;
            default:
                return false;
        }
    }

    public static String setScoreDigits(Workout workout) {
        String workoutType = workout.getType();
        switch (workoutType) {
            case "for-time":
                return "0123456789:";
            case "amrap":
                return "0123456789-";
            case "emom":
                return "0123456789";
            case "for-weight":
                return "0123456789.";
            case "sets":
                return "0123456789.-";
            default:
                return "0123456789:.-";
        }
    }
}
