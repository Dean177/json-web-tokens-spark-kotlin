package SpaceView

import com.google.gson.Gson
import generateToken
import io.jsonwebtoken.*
import io.jsonwebtoken.impl.crypto.MacProvider
import jwtFilter
import spark.Request
import spark.Response
import spark.Spark.*

val signatureAlgorithm = SignatureAlgorithm.HS512
val key = MacProvider.generateKey(signatureAlgorithm)

fun main(args: Array<String>) {
  staticFileLocation("public")

  val onSecured = { req: Request, res: Response ->
    ""
  }

  get("/", { req, res -> "Hello World" })

  get("/login", { req, res -> generateToken() })

  before("/api/*", { req, res -> jwtFilter(req, res) })

  get("/api/secured", onSecured, { Gson().toJson(it) })

  after { request, response -> response.header("Content-Encoding", "gzip") }
}

