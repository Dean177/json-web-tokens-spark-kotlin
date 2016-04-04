import SpaceView.key
import SpaceView.signatureAlgorithm
import io.jsonwebtoken.*
import spark.Request
import spark.Response
import spark.Spark
import java.util.*


fun generateToken(): String {
  return Jwts.builder()
      .setSubject("Subject")
      .setIssuedAt(Date())
      .setId("123456")
      .setExpiration(Calendar.getInstance().apply { add(Calendar.MINUTE, 15) }.time)
      .setClaims(mapOf("roles" to arrayOf("user", "write", "admin")))
      .signWith(signatureAlgorithm, key)
      .compact()
}

fun decodeToken(token: String): Jwt<*, *>? {
  return Jwts.parser().setSigningKey(key).parse(token)
}

fun jwtFilter(request: Request, response: Response): Unit {
  val authHeader: String? = request.headers("Authentication")
  val token: String? = authHeader?.split("Bearer ")?.filter { it.length > 0 }?.first()
  if (token == null) {
    Spark.halt(401, "No token")
  } else {
    try {
      val decoded = decodeToken(token)
      println("""
        Header: ${decoded?.header}
        Body: ${decoded?.body}
      """)
    } catch (ex: SignatureException) {
      Spark.halt(401, "Token invalid")
    }
  }
}