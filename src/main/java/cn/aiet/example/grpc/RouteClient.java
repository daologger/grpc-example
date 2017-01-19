package cn.aiet.example.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

/**
 * @author aiet
 */
public class RouteClient {

  private RouteGrpc.RouteBlockingStub blockingStub;
  private RouteGrpc.RouteFutureStub futureStub;
  private RouteGrpc.RouteStub stub;
  private ManagedChannelBuilder channelBuilder;

  public RouteClient(int port){
    this.channelBuilder = ManagedChannelBuilder.forAddress("localhost", port).usePlaintext(true);
  }

  public RouteGrpc.RouteFutureStub connectAsync() throws Exception{
    ManagedChannel channel = channelBuilder.build();
    futureStub = RouteGrpc.newFutureStub(channel);
    return futureStub;
  }

  public RouteGrpc.RouteBlockingStub connectBlocking() throws Exception{
    ManagedChannel channel = channelBuilder.build();
    blockingStub = RouteGrpc.newBlockingStub(channel);
    return blockingStub;
  }

  public RouteGrpc.RouteStub connect() throws Exception{
    ManagedChannel channel = channelBuilder.build();
    stub = RouteGrpc.newStub(channel);
    return stub;
  }

}
