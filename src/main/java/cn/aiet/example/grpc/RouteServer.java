package cn.aiet.example.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerServiceDefinition;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author aiet
 */
public class RouteServer extends RouteGrpc.RouteImplBase {

  private Logger LOG = LoggerFactory.getLogger(this.getClass());

  private Server server;

  public RouteServer(int port) {
    server = ServerBuilder.forPort(port).addService(this).build();
  }

  public void start() throws Exception {
    server.start().awaitTermination();
  }

  public void stop() throws Exception {
    server.shutdown();
  }


  @Override
  public void getFeature(RouteGuideProto.Point request, StreamObserver<RouteGuideProto.Feature> responseObserver) {
    LOG.trace("client requesting with {}", request);
    responseObserver.onNext(RouteGuideProto.Feature.newBuilder().setName("test").setLoc(RouteGuideProto.Point.newBuilder().setLat(2).setLng(3).build()).build());
    responseObserver.onCompleted();
  }

  @Override
  public void listFeatures(RouteGuideProto.Rectangle request, StreamObserver<RouteGuideProto.Feature> responseObserver) {
    LOG.trace("client requesting with {}", request);
    responseObserver.onNext(RouteGuideProto.Feature.newBuilder().setName("test1").setLoc(RouteGuideProto.Point.newBuilder().setLat(2).setLng(3).build()).build());
    responseObserver.onNext(RouteGuideProto.Feature.newBuilder().setName("test2").setLoc(RouteGuideProto.Point.newBuilder().setLat(4).setLng(6).build()).build());
    responseObserver.onNext(RouteGuideProto.Feature.newBuilder().setName("test3").setLoc(RouteGuideProto.Point.newBuilder().setLat(8).setLng(9).build()).build());
    responseObserver.onCompleted();
  }

  @Override
  public StreamObserver<RouteGuideProto.Point> recordRoute(StreamObserver<RouteGuideProto.RouteSummary> responseObserver) {
    LOG.info("streaming routes for summary...");
    return new StreamObserver<RouteGuideProto.Point>() {
      int distance = 0;
      int elapse = 0;
      int featureCounter;
      int pointCounter;
      RouteGuideProto.Point prevPoint;
      long prevTimeInMillis;

      @Override
      public void onNext(RouteGuideProto.Point value) {
        pointCounter++;
        if (value.getLng() % 3 == 0 && value.getLat() % 2 == 0) featureCounter++;
        if (prevPoint != null) {
          distance += (int) Math.sqrt(Math.pow(value.getLat() - prevPoint.getLat(), 2) + Math.pow(value.getLng() - prevPoint.getLng(), 2));
          long currentTimeInMillis = System.currentTimeMillis();
          elapse += currentTimeInMillis - prevTimeInMillis;
          prevTimeInMillis = currentTimeInMillis;
          LOG.debug("current distance {}", distance);
        }
        prevPoint = value;
        prevTimeInMillis = System.currentTimeMillis();
      }

      @Override
      public void onError(Throwable t) {
        LOG.error("client error ", t);
      }

      @Override
      public void onCompleted() {
        responseObserver.onNext(RouteGuideProto.RouteSummary.newBuilder()
            .setDistance(distance).setElapse(elapse / 1000).setFeatureCount(featureCounter).setPointCount(pointCounter)
            .build());
        responseObserver.onCompleted();
      }
    };
  }

  @Override
  public StreamObserver<RouteGuideProto.RouteNote> routeChat(StreamObserver<RouteGuideProto.RouteNote> responseObserver) {
    return new StreamObserver<RouteGuideProto.RouteNote>() {
      @Override
      public void onNext(RouteGuideProto.RouteNote value) {
        LOG.debug("note incoming {} @{}", value, System.currentTimeMillis());
        responseObserver.onNext(
            RouteGuideProto.RouteNote.newBuilder().setLoc(value.getLoc()).setMsg("from server: " + value.getMsg()).build()
        );
      }

      @Override
      public void onError(Throwable t) {
        LOG.error("client error ", t);
      }

      @Override
      public void onCompleted() {
        responseObserver.onCompleted();
        LOG.info("notes incoming done @{}", System.currentTimeMillis());
      }
    };
  }

  @Override
  public ServerServiceDefinition bindService() {
    return super.bindService();
  }
}
