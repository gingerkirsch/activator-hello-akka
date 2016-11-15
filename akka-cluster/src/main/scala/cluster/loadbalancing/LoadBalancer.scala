package cluster.loadbalancing

/**
  * Created by sankova on 11/15/16.
  */

object LoadBalancer extends App {

  //initiate three nodes from backend
  BackEnd.initiate(2551)

  BackEnd.initiate(2552)

  BackEnd.initiate(2561)

  //initiate frontend node
  FrontEnd.initiate()

}