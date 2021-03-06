package com.optrak.hello.impl

import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import com.lightbend.lagom.scaladsl.server.ServerServiceCall
import com.optrak.hello.api.HelloService
import com.optrak.auth.{SimpleServerSecurity, SimpleSetAuthenticator}

/**
  * Implementation of the HelloService.
  */
class HelloServiceImpl(persistentEntityRegistry: PersistentEntityRegistry) extends HelloService with SimpleServerSecurity
  with SimpleSetAuthenticator {

  override def hello(id: String) = ServiceCall { _ =>
    // Look up the Hello entity for the given ID.
    val ref = persistentEntityRegistry.refFor[HelloEntity](id)

    // Ask the entity the Hello command.
    ref.ask(Hello(id, None))
  }

  override def authHello = authenticated { userName =>
    ServerServiceCall { _ =>
      // Look up the Hello entity for the given ID.
      val ref = persistentEntityRegistry.refFor[HelloEntity](userName)

      // Ask the entity the Hello command.
      ref.ask(Hello(userName, None))
    }
  }

  override def useGreeting(id: String) = ServiceCall { request =>
    // Look up the Hello entity for the given ID.
    val ref = persistentEntityRegistry.refFor[HelloEntity](id)

    // Tell the entity to use the greeting message specified.
    ref.ask(UseGreetingMessage(request.message))
  }
}
