package client

/**
  * Created by sankova on 11/4/16.
  */
case class IpInfo (ip: String)

object JsonProtocol extends DefaultJsonProtocol{
  implicit val format = jsonFormat1(IpInfo.apply)
}
