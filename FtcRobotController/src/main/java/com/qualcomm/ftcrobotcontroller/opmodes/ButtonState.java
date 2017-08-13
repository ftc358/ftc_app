package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.Gamepad;

/**
 * Created by jamesliu on 1/8/16.
 */
public class ButtonState {
    public ButtonState(){
        timestamp=System.currentTimeMillis();
    }

    public boolean a_press(){
        return(a&!preva);
    }
    public boolean a_release(){
        return(!a&preva);
    }
    public boolean b_press(){
        return(b&!prevb);
    }
    public boolean b_release(){
        return(!b&prevb);
    }
    public boolean x_press(){
        return(x&!prevx);
    }
    public boolean x_release(){
        return(!x&prevx);
    }
    public boolean y_press(){
        return(y&!prevy);
    }
    public boolean y_release(){
        return(!y&prevy);
    }
    public boolean up_press(){
        return(up&!prevup);
    }
    public boolean up_release(){
        return(!up&prevup);
    }
    public boolean down_press(){
        return(down&!prevdown);
    }
    public boolean down_release(){
        return(!down&prevdown);
    }
    public boolean left_press(){
        return(left&!prevleft);
    }
    public boolean left_release(){
        return(!left&prevleft);
    }
    public boolean right_press(){
        return(right&!prevright);
    }
    public boolean right_release(){
        return(!right&prevright);
    }
    public boolean guide_press(){
        return(guide&!prevguide);
    }
    public boolean guide_release(){
        return(!guide&prevguide);
    }
    public boolean start_press(){
        return(start&!prevstart);
    }
    public boolean start_release(){
        return(!start&prevstart);
    }
    public boolean back_press(){
        return(back&!prevback);
    }
    public boolean back_release(){
        return(!back&prevback);
    }
    public boolean left_bumper_press(){
        return(left_bumper&!prevleft_bumper);
    }
    public boolean left_bumper_release(){
        return(!left_bumper&prevleft_bumper);
    }
    public boolean right_bumper_press(){
        return(right_bumper&!prevright_bumper);
    }
    public boolean right_bumper_release(){
        return(!right_bumper&prevright_bumper);
    }
    public boolean left_stick_button_press(){
        return(left_stick_button&!prevleft_stick_button);
    }
    public boolean left_stick_button_release(){
        return(!left_stick_button&prevleft_stick_button);
    }
    public boolean right_stick_button_press(){
        return(right_stick_button&!prevright_stick_button);
    }
    public boolean right_stick_button_release(){
        return(!right_stick_button&prevright_stick_button);
    }

    public void pushButtonHistory(){
        prevleft_stick_x=left_stick_x;
        prevleft_stick_y=left_stick_y;
        prevright_stick_x=right_stick_x;
        prevright_stick_y=right_stick_y;
        prevup=up;
        prevdown=down;
        prevleft=left;
        prevright=right;
        preva=a;
        prevb=b;
        prevx=x;
        prevy=y;
        prevguide=guide;
        prevstart=start;
        prevback=back;
        prevleft_bumper=left_bumper;
        prevright_bumper=right_bumper;
        prevleft_stick_button=left_stick_button;
        prevright_stick_button=right_stick_button;
        prevleft_trigger=left_trigger;
        prevright_trigger=right_trigger;
        prevtimestamp=timestamp;
    }

    public void update(Gamepad gamepad){
        left_stick_x=gamepad.left_stick_x;
        left_stick_y=gamepad.left_stick_y;
        right_stick_x=gamepad.right_stick_x;
        right_stick_y=gamepad.right_stick_y;
        up=gamepad.dpad_up;
        down=gamepad.dpad_down;
        left=gamepad.dpad_left;
        right=gamepad.dpad_right;
        a=gamepad.a;
        b=gamepad.b;
        x=gamepad.x;
        y=gamepad.y;
        guide=gamepad.guide;
        start=gamepad.start;
        back=gamepad.back;
        left_bumper=gamepad.left_bumper;
        right_bumper=gamepad.right_bumper;
        left_stick_button=gamepad.left_stick_button;
        right_stick_button=gamepad.right_stick_button;
        left_trigger=gamepad.left_trigger;
        right_trigger=gamepad.right_trigger;
        timestamp=gamepad.timestamp;
    }

    public float left_stick_x;
    public float left_stick_y;
    public float right_stick_x;
    public float right_stick_y;
    public boolean up;
    public boolean down;
    public boolean left;
    public boolean right;
    public boolean a;
    public boolean b;
    public boolean x;
    public boolean y;
    public boolean guide;
    public boolean start;
    public boolean back;
    public boolean left_bumper;
    public boolean right_bumper;
    public boolean left_stick_button;
    public boolean right_stick_button;
    public float left_trigger;
    public float right_trigger;
    public long timestamp;

    public float prevleft_stick_x;
    public float prevleft_stick_y;
    public float prevright_stick_x;
    public float prevright_stick_y;
    public boolean prevup;
    public boolean prevdown;
    public boolean prevleft;
    public boolean prevright;
    public boolean preva;
    public boolean prevb;
    public boolean prevx;
    public boolean prevy;
    public boolean prevguide;
    public boolean prevstart;
    public boolean prevback;
    public boolean prevleft_bumper;
    public boolean prevright_bumper;
    public boolean prevleft_stick_button;
    public boolean prevright_stick_button;
    public float prevleft_trigger;
    public float prevright_trigger;
    public long prevtimestamp;
}
