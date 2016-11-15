package cluster.frontendbackend

import cluster.Add

/**
  * Created by sankova on 11/15/16.
  */
object ClusterApp extends App {

  //initiate frontend node
  FrontEnd.initiate()

  //initiate three nodes from backend
  BackEnd.initiate(2552)

  BackEnd.initiate(2560)

  BackEnd.initiate(2561)

  Thread.sleep(10000)

  FrontEnd.getFrontend ! Add(2, 4)

}