package com.twitter.hello

import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller

class HelloWorldController extends Controller {

  get("/hi") { request: Request =>
    response.temporaryRedirect.location("mailto:support@sport-loto.ru")
  }
}
