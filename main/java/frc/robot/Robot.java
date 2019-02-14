/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

// java docs http://first.wpi.edu/FRC/roborio/release/docs/java/

package frc.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import com.ctre.phoenix.motorcontrol.can.*;
import java.util.TimerTask;
import java.util.Timer;

public class Robot extends TimedRobot {
  private DifferentialDrive m_myRobot;
  private Joystick m_leftStick;
  private Joystick m_rightStick;
  private Joystick m_lift;
  private WPI_VictorSPX FL = new WPI_VictorSPX(0);
  private WPI_VictorSPX BL = new WPI_VictorSPX(1);
  private WPI_VictorSPX FR = new WPI_VictorSPX(2);
  private WPI_VictorSPX BR = new WPI_VictorSPX(3);
  private WPI_VictorSPX Lift = new WPI_VictorSPX(4);
  private SpeedControllerGroup leftSide = new SpeedControllerGroup(FL, BL);
  private SpeedControllerGroup rightSide = new SpeedControllerGroup(FR, BR);
  private DigitalInput bottom = new DigitalInput(0);
  private DigitalInput top = new DigitalInput(1);
  private DoubleSolenoid grabber = new DoubleSolenoid(0, 1);
  private DoubleSolenoid pushers = new DoubleSolenoid(2, 3);
  private DoubleSolenoid track = new DoubleSolenoid(4, 5);
  private Timer lag = new Timer();

  @Override
  public void robotInit() {
    m_myRobot = new DifferentialDrive(leftSide, rightSide);
    m_leftStick = new Joystick(0);
    m_rightStick = new Joystick(1);
    m_lift = new Joystick(2);
  }
  
/*
  solenoid config:
  
  pre-grab/idle - neither
  holding - grabber on pushers off
  placing - grabber off then pushers on
*/

  @Override
  public void teleopPeriodic() {
    m_myRobot.tankDrive(m_leftStick.getY(), m_rightStick.getY());
    if ((top.get() && m_lift.getY() > 0.0) || (bottom.get() && m_lift.getY() < 0.0)){
      Lift.set(0.0);
    }
    else {
      Lift.set(m_lift.getY());    
    }
    if (m_leftStick.getTrigger() == true && m_rightStick.getTrigger() == false) {
      grabber.set(Value.kReverse);
      lag.schedule(new TimerTask(){
  
        @Override
        public void run() {
          pushers.set(Value.kForward);
        }
      }, 10);
      lag.schedule(new TimerTask(){
  
        @Override
        public void run() {
          pushers.set(Value.kReverse);
        }
      }, 10);
    }
    else if (m_leftStick.getTrigger() == false && m_rightStick.getTrigger() == true) {
      pushers.set(Value.kReverse);
      lag.schedule(new TimerTask(){
  
        @Override
        public void run() {
          grabber.set(Value.kForward);
        }
      }, 10);
    }
    if (m_leftStick.getRawButton(2) == false && m_rightStick.getRawButton(2) == true) {
      track.set(Value.kForward);
    }
    else if (m_leftStick.getRawButton(2) == true && m_rightStick.getRawButton(2) == false) {
      track.set(Value.kReverse);
    }
  }
}
