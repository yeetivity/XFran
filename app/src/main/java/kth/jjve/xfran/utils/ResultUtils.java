package kth.jjve.xfran.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import kth.jjve.xfran.R;
import kth.jjve.xfran.adapters.AppCtx;
import kth.jjve.xfran.models.Result;
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

    public static String scalingString(Result mResult) {
        String isScaledStr = "No";
        if (mResult.isScaled()) {
            isScaledStr = "Yes";
        }
        String scaledStr = AppCtx.getContext().getResources().getString(R.string.scaled) + " " + isScaledStr;
        return scaledStr;
    }

    public static String feelScoreString(Result mResult) {
        int feelScore = mResult.getFeelScore();
        String feelScoreStr;
        switch (feelScore) {
            case 1:
                feelScoreStr = "Very easy (1/5)";
                break;
            case 2:
                feelScoreStr = "Easy (2/5)";
                break;
            case 3:
                feelScoreStr = "Medium (3/5)";
                break;
            case 4:
                feelScoreStr = "Hard (4/5)";
                break;
            case 5:
                feelScoreStr = "Very hard (5/5)";
                break;
            default:
                feelScoreStr = "No score";
        }
        return AppCtx.getContext().getResources().getString(R.string.result_feel_score) + " " + feelScoreStr;
    }

    public static String commentsString(Result mResult) {
        return AppCtx.getContext().getResources().getString(R.string.result_comments) + " " + mResult.getComments();
    }

    public static String dateString(Result mResult) {
        // changes format of date from 2021-12-23 to 21/12/23
        try {
            LocalDate date = LocalDate.parse(mResult.getDate());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM\nyyyy");
            return date.format(formatter);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
