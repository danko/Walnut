/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
//import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.Ultrasonic;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.GenericHID.Hand;

//import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Encoder;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

  // Driver Station setup
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
 
  private Timer timer;
  private Ultrasonic f_ultrasonic;

  // for use with two joysticks
  private Joystick stickLeft;
  private Joystick stickRight;

  //For use with Xbox controller
  private XboxController xbox;

  // motor control 4 wheel drive - tank drive
  private Talon m_frontLeft;
  private Talon m_rearLeft;
  private SpeedControllerGroup m_left;
  private Talon m_frontRight;
  private Talon m_rearRight;
  private SpeedControllerGroup m_right;
  private DifferentialDrive m_drive;
  private Encoder encoder_backRight;
  private Encoder encoder_backLeft;
  private Encoder encoder_frontLeft;
  private Encoder encoder_frontRight;

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.addDefault("Default Auto", kDefaultAuto);
    m_chooser.addObject("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    // joysticks plugged into two USB ports
    //stickLeft = new Joystick(0);
    //stickRight = new Joystick(1);

    xbox = new XboxController(0);

    // timer used by autonomous method
    timer = new Timer();

    // Motors on left side of robot grouped together
    m_frontLeft = new Talon(0);
    m_rearLeft = new Talon(2);
    m_left = new SpeedControllerGroup(m_frontLeft, m_rearLeft);
 
    // Motors on right side of robot grouped together
    m_frontRight = new Talon(1);
    m_rearRight = new Talon(3);
    m_right = new SpeedControllerGroup(m_frontRight, m_rearRight);

    // Group right and left sides into one drive object
    m_drive = new DifferentialDrive(m_left, m_right);

    //Start continuous capture of images from USB camera
    CameraServer.getInstance().startAutomaticCapture(0);
    f_ultrasonic = new Ultrasonic(9,8); // ping, echo
    f_ultrasonic.setAutomaticMode(true);
    f_ultrasonic.setEnabled(true);

    encoder_backRight = new Encoder(1,0);
    encoder_backLeft = new Encoder(3,2);
    encoder_frontLeft = new Encoder(5,4);
    encoder_frontRight = new Encoder(6,7);



}
  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    System.out.println("Auto selected: " + m_autoSelected);
    timer.reset();
    timer.start();
    encoder_backRight.reset();
    encoder_backLeft.reset();
  }
public void testencoder(){
  /*if (timer.get() < 1){
    m_drive.curvatureDrive(.15, 0, false);
  }
  int backrightvalue = encoder_backRight.get();
  int backleftvalue = encoder_backLeft.get();
  int frontrightvalue = encoder_frontRight.get();
  int frontleftvalue = encoder_frontLeft.get();
  System.out.println("inside test encoder********************************************");
  System.out.println(backrightvalue);
  System.out.println(backleftvalue);
  System.out.println(frontrightvalue);
  System.out.println(frontleftvalue);*/
}
  /*public void teleopInit() {
  }*/

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    System.out.println("Peanut 4 auto");
    int i = 1;
    double f_range = f_ultrasonic.getRangeInches();
    System.out.println(f_range);
    System.out.println(f_ultrasonic.isRangeValid());
    System.out.println(f_ultrasonic.isEnabled());
    if (f_ultrasonic.isRangeValid()) {
      if (i == 1 && f_range > 9) {
        m_drive.curvatureDrive(0.15, -0.275, false);
      }
      else if (f_range < 6) {
        m_drive.curvatureDrive(-0.15, 0.0, false);
      }
      else {
        m_drive.curvatureDrive(0, 0, false);
      }
    }
    //testencoder();
  /*int backrightvalue = encoder_backRight.get();
  int backleftvalue = encoder_backLeft.get();
  int frontrightvalue = encoder_frontRight.get();
  int frontleftvalue = encoder_frontLeft.get();
  int rightturning = backrightvalue + frontrightvalue;
  int leftturning = backleftvalue + frontleftvalue;
  System.out.println(rightturning);
  System.out.println(leftturning);
  if(rightturning > leftturning) {
    m_drive.curvatureDrive(0.15, 0.2, false);
  }
  else if(leftturning > rightturning) {
    m_drive.curvatureDrive(0.15, -0.4, false);
  }
  else if(leftturning == rightturning) {
    m_drive.curvatureDrive(0.15, 0.0, false);
  }
  encoder_backRight.reset();
  encoder_backLeft.reset();
  encoder_frontRight.reset();
  encoder_frontLeft.reset();*/
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    // slow down drive by 50%
    //m_drive.tankDrive(stickLeft.getY() * 0.5, stickRight.getY() * 0.5, true);

    double leftSpeed = -0.5 * xbox.getY(Hand.kLeft);
    double rightSpeed = -0.5 * xbox.getY(Hand.kRight);

    //double leftSpeed = -0.5*stickLeft.getY();
    //double rightSpeed = -0.5*stickRight.getY();
    m_drive.tankDrive(leftSpeed, rightSpeed);
    //Timer.delay(0.01);
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }

  @Override
  public void testInit(){
    m_frontLeft.set(0.2);
    m_rearLeft.set(0.2);
    m_frontRight.set(0.2);
    m_rearRight.set(0.2);

  }
}