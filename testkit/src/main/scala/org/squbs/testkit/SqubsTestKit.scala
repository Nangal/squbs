package org.squbs.testkit

import scala.concurrent.duration._
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.{Suite, BeforeAndAfterAll}
import org.squbs.unicomplex.{Bootstrap, Unicomplex}

/**
 * Copyright (c) 2013 eBay, Inc.
 * All rights reserved.
 *
 * Contributors:
 * asucharitakul
 */
object SqubsTestKit {
  sys.addShutdownHook {
    Unicomplex.actorSystem.shutdown()
  }
  Bootstrap.main(Array.empty[String])

  private def checkInit(instance: SqubsTestKit) {
    // No op. Just need to ensure SqubsTestKit object is initialized.
  }
}

abstract class SqubsTestKit extends TestKit(Unicomplex.actorSystem)
with ImplicitSender with Suite with BeforeAndAfterAll {

  val debugTimeout = 10000.seconds

  val debugMode = java.lang.management.ManagementFactory.getRuntimeMXBean.
    getInputArguments.toString.indexOf("jdwp") >= 0

  if (debugMode) println(
    "\n##################\n" +
    s"IMPORTANT: Detected system running in debug mode. All timeouts overridden to $debugTimeout.\n" +
    "##################\n\n")



  override protected def beforeAll() {
    SqubsTestKit.checkInit(this)
  }

  override def receiveOne(max: Duration) =
    if (debugMode) super.receiveOne(debugTimeout)
    else super.receiveOne(max)

}