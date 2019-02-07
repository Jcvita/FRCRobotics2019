/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import com.ctre.phoenix.motorcontrol.can.*;


/**
 * This is a demo program showing the use of the RobotDrive class, specifically
 * it contains the code necessary to operate a robot with tank drive.
 */
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

  @Override
  public void robotInit() {
    m_myRobot = new DifferentialDrive(leftSide, rightSide);
    m_leftStick = new Joystick(0);
    m_rightStick = new Joystick(1);
    m_lift = new Joystick(2);
  }

  

  @Override
  public void teleopPeriodic() {
    m_myRobot.tankDrive(m_leftStick.getY(), m_rightStick.getY());
    if ((top.get() && m_lift.getY() > 0.0) || (bottom.get() && m_lift.getY() < 0.0)){
      Lift.set(0.0);
    }
    else {
      Lift.set(m_lift.getY());    
    }
    
  }
}
