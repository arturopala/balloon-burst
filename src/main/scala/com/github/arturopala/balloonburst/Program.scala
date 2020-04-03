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

import scala.annotation.tailrec

/** Reactive functional program essence. */
trait Program {

  type State
  type Action
  type Effect
  type Program = (State, Action) => (State, List[Effect])
  type Environment = Effect => List[Action]

  val initialAction: Action
  val initialState: State

  val program: Program

  def runIn(environment: Environment): State = {

    @tailrec
    def evaluate(state: State, queue: Vector[Action]): State =
      if (queue.isEmpty) state
      else {
        val action = queue.head
        val (newState, effects) = program(state, action)
        val newActions = effects.flatMap(environment)
        evaluate(newState, queue.tail ++ newActions)
      }

    evaluate(initialState, Vector(initialAction))
  }

}
