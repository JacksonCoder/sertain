@file:Suppress("unused", "RedundantVisibilityModifier")
@file:JvmName("SubsystemUtils")
package org.sertain.command

import org.sertain.Controllable
import edu.wpi.first.wpilibj.command.Subsystem as WpiLibSubsystem

/** @see edu.wpi.first.wpilibj.command.Subsystem */
public abstract class Subsystem : WpiLibSubsystem(), Controllable {
    /** @see edu.wpi.first.wpilibj.command.Subsystem.setDefaultCommand */
    open val defaultCommand: CommandBase? = null

    override fun initDefaultCommand() = setDefaultCommand(defaultCommand?.mirror)
}
