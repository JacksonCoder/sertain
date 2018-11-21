@file:Suppress("unused", "RedundantVisibilityModifier")
@file:JvmName("RobotUtils")
package org.sertain

import edu.wpi.first.wpilibj.IterativeRobot
import org.sertain.command.Subsystem

enum class RobotState {
    DISABLED, AUTO, TELEOP
}

/**
 * The base interface for robot control.
 */
public interface Controllable {
    public fun onCreate() = Unit

    public fun onEnabled() = Unit

    public fun onDisabled() = Unit

    public fun onAutoStart() = Unit

    public fun onTeleopStart() = Unit

    public fun onAutoEnd() = Unit

    public fun onTeleopEnd() = Unit

    public fun onTick() = Unit

    public fun onTickAuto() = Unit

    public fun onTickTeleop() = Unit

    public fun onTickDisabled() = Unit
}

/**
 * An abstract wrapper over IterativeRobot that forwards events to the Controllable implementation
 */
abstract class WPIRobotLink : Controllable, IterativeRobot() {
    private var state: RobotState = RobotState.DISABLED
        set(value) {
            if (value != field) {
                when (field) {
                    RobotState.DISABLED -> Unit
                    RobotState.AUTO -> onAutoEnd()
                    RobotState.TELEOP -> onTeleopEnd()
                }
                field = value
            }
        }

    override fun robotInit() = onCreate()

    override fun robotPeriodic() = onTick()

    override fun disabledInit() {
        state = RobotState.DISABLED
        onDisabled()
    }

    override fun disabledPeriodic() = onTickDisabled()

    override fun autonomousInit() {
        state = RobotState.AUTO
        onEnabled() // Should we be calling this here?
        onAutoStart()
    }

    override fun autonomousPeriodic() = onTickAuto()

    override fun teleopInit() {
        state = RobotState.TELEOP
        onTeleopStart()
    }

    override fun teleopPeriodic() = onTickTeleop()
}

public abstract class Robot(vararg subsystems: Subsystem) : WPIRobotLink() {
    private val robotSubsystems = subsystems

    private inline fun callSubsystems(func: Controllable.() -> Unit) {
        for (system in robotSubsystems) {
            system.func()
        }
    }

    override fun onTeleopStart() {
        super.onTeleopStart()
        callSubsystems { onTeleopStart() }
    }

    override fun onTickTeleop() {
        super.onTickTeleop()
        callSubsystems { onTickTeleop() }
    }

    override fun onTeleopEnd() {
        super.onTeleopEnd()
        callSubsystems { onTeleopEnd() }
    }

    override fun onAutoStart() {
        super.onAutoStart()
        callSubsystems { onAutoStart() }
    }

    override fun onTickAuto() {
        super.onTickAuto()
        callSubsystems { onTickAuto() }
    }

    override fun onAutoEnd() {
        super.onAutoEnd()
        callSubsystems { onAutoEnd() }
    }
}

object Robo: Robot() {
    override fun onAutoStart() {
        print("Hello world!")
    }
}