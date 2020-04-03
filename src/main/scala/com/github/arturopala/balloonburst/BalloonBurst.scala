/*
 * Copyright 2020 Artur Opala
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.arturopala.balloonburst

import scala.util.Try

/**
  * The purpose of balloon burst is to measure propensity to risk,
  * in it you must inflate a balloon as many times as you dare and
  * stop when you think it has been inflated as much as it will allow.
  *
  * If you manage to inflate a balloon and bank it you get the number
  * of inflations added to your score. If you inflate the balloon beyond
  * it's limit it will burst, and you don't get to bank anything.
  */
object BalloonBurst extends Program {

  type Action = String
  sealed trait State
  sealed trait Effect

  case object Start extends State
  case object InputBalloonList extends State
  case class InflatingBalloons(limits: Seq[Int], tries: Int, score: Int) extends State
  case class Score(score: Int) extends State

  case object Ask extends Effect
  case class Show(msg: String) extends Effect

  val initialAction: Action = ""
  val initialState: State = Start

  val program: Program = {

    // Ask user to input a list of balloon inflate limits
    case (Start, _) => (InputBalloonList, Ask :: Nil)

    // Each number is representing a balloon, and it's value the
    // maximum number of inflations it can withstand.
    case (InputBalloonList, list) =>
      Try(list.trim.split("\\s+").map(_.toInt).toList)
        .map(limits => (InflatingBalloons(limits, 0, 0), Ask :: Nil))
        .getOrElse((InputBalloonList, Ask :: Nil))

    // Then an input of INFLATE will inflate the current balloon
    case (InflatingBalloons(limits, tries, score), "INFLATE") =>
      limits match {
        case Nil => program(Score(score), "")
        case limit :: next =>
          if (tries + 1 <= limit) (InflatingBalloons(limits, tries + 1, score), Ask :: Nil)
          else if (next.nonEmpty) (InflatingBalloons(next, 0, score), Show("BURST") :: Ask :: Nil)
          else program(Score(score), "") after Show("BURST")
      }

    // An input of BANK will bank the current score and move on to the next balloon.
    // If the number of inflate attempts goes over that balloons burst limit a BURST must
    // be printed to the user, input commands after that go to the next balloon.
    case (InflatingBalloons(limits, tries, score), "BANK") =>
      limits match {
        case Nil      => program(Score(score), "")
        case _ :: Nil => program(Score(score + tries), "")
        case _ :: next =>
          (InflatingBalloons(next, 0, score + tries), Ask :: Nil)
      }

    // Once there are no balloons left the final score is printed.
    case (Score(score), _) => (Score(score), Show(s"SCORE: $score") :: Nil)

    // Fallback, ask user again if action not understood
    case (state, _) => (state, Ask :: Nil)
  }

}
