/*
 * Copyright 2007-2008 WorldWide Conferencing, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 */
package net.liftweb.http

import _root_.scala.xml.Node
import _root_.net.liftweb.util._
import _root_.javax.servlet.http.Cookie

/**
 * 200 response but without body.
 */
case class OkResponse() extends LiftResponse {
  def toResponse = InMemoryResponse(Array(), Nil, Nil, 200)
}

/**
 * 201 Created Response
 *
 * The Resource was created. We then return the resource, post-processing, to
 * the client. Usually used with HTTP PUT.
 */
case class CreatedResponse(xml: Node, mime: String) extends NodeResponse {
  def docType = Empty
  def code = 201
  def headers = List("Content-Type" -> mime)
  def cookies = Nil
  def out = xml
}

/**
 * 301 Redirect.
 */
case class PermRedirectResponse(uri: String, request: Req, cookies: Cookie*) extends LiftResponse {
  def toResponse = InMemoryResponse(Array(), List("Location" -> request.updateWithContextPath(uri)), cookies.toList, 301)
}

/**
 * 400 Bad Request
 *
 * Your Request was missing an important element. Use this as a last resort if
 * the request appears incorrect.
 */
case class BadResponse() extends LiftResponse {
  def toResponse = InMemoryResponse(Array(), Nil, Nil, 400)
}

/**
 * 401 Unauthorized Response.
 */
case class UnauthorizedResponse(realm: String) extends LiftResponse {
  def toResponse = InMemoryResponse(Array(), List("WWW-Authenticate" -> ("Basic realm=\"" + realm + "\"")), Nil, 401)
}

object Qop extends Enumeration(0, "auth", "auth-int", "auth,auth-int") {
  val AUTH, AUTH_INT, AUTH_AND_AUTH_INT = Value
}

/**
 * 401 Unauthorized Response.
 */
case class UnauthorizedDigestResponse(override val realm: String, qop: Qop.Value, nonce: String, opaque: String) extends UnauthorizedResponse(realm) {
  override def toResponse = InMemoryResponse(Array(), List("WWW-Authenticate" -> (
        "Digest realm=\"" + realm + "\", " +
        "qop=\"" + qop + "\", " +
        "nonce=\"" + nonce + "\", " +
        "opaque=\"" + opaque + "\""
      )), Nil, 401)
}

/**
 * 404 Not Found
 *
 * The requested Resource does not exist.
 */
case class NotFoundResponse() extends LiftResponse {
  def toResponse = InMemoryResponse(Array(), Nil, Nil, 404)
}

/**
 * 405 Method Not Allowed
 *
 * This Resource does not allow this method. Use this when the resource can't
 * understand the method no matter the circumstances.
 */
case class MethodNotAllowedResponse() extends LiftResponse {
  def toResponse = InMemoryResponse(Array(), Nil, Nil, 405)
}

/**
 * 410 Resource Gone
 *
 * The requested Resource used to exist but no longer does.
 */
case class GoneResponse() extends LiftResponse {
  def toResponse = InMemoryResponse(Array(), Nil, Nil, 410)
}
