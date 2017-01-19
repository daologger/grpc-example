package cn.aiet.example.grpc;

import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.0.3)",
    comments = "Source: route.proto")
public class RouteGrpc {

  private RouteGrpc() {}

  public static final String SERVICE_NAME = "routeguide.Route";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<RouteGuideProto.Point,
      RouteGuideProto.Feature> METHOD_GET_FEATURE =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "routeguide.Route", "getFeature"),
          io.grpc.protobuf.ProtoUtils.marshaller(RouteGuideProto.Point.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(RouteGuideProto.Feature.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<RouteGuideProto.Rectangle,
      RouteGuideProto.Feature> METHOD_LIST_FEATURES =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING,
          generateFullMethodName(
              "routeguide.Route", "listFeatures"),
          io.grpc.protobuf.ProtoUtils.marshaller(RouteGuideProto.Rectangle.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(RouteGuideProto.Feature.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<RouteGuideProto.Point,
      RouteGuideProto.RouteSummary> METHOD_RECORD_ROUTE =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING,
          generateFullMethodName(
              "routeguide.Route", "recordRoute"),
          io.grpc.protobuf.ProtoUtils.marshaller(RouteGuideProto.Point.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(RouteGuideProto.RouteSummary.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<RouteGuideProto.RouteNote,
      RouteGuideProto.RouteNote> METHOD_ROUTE_CHAT =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING,
          generateFullMethodName(
              "routeguide.Route", "routeChat"),
          io.grpc.protobuf.ProtoUtils.marshaller(RouteGuideProto.RouteNote.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(RouteGuideProto.RouteNote.getDefaultInstance()));

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static RouteStub newStub(io.grpc.Channel channel) {
    return new RouteStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static RouteBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new RouteBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary and streaming output calls on the service
   */
  public static RouteFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new RouteFutureStub(channel);
  }

  /**
   */
  public static abstract class RouteImplBase implements io.grpc.BindableService {

    /**
     */
    public void getFeature(RouteGuideProto.Point request,
        io.grpc.stub.StreamObserver<RouteGuideProto.Feature> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_GET_FEATURE, responseObserver);
    }

    /**
     */
    public void listFeatures(RouteGuideProto.Rectangle request,
        io.grpc.stub.StreamObserver<RouteGuideProto.Feature> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_LIST_FEATURES, responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<RouteGuideProto.Point> recordRoute(
        io.grpc.stub.StreamObserver<RouteGuideProto.RouteSummary> responseObserver) {
      return asyncUnimplementedStreamingCall(METHOD_RECORD_ROUTE, responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<RouteGuideProto.RouteNote> routeChat(
        io.grpc.stub.StreamObserver<RouteGuideProto.RouteNote> responseObserver) {
      return asyncUnimplementedStreamingCall(METHOD_ROUTE_CHAT, responseObserver);
    }

    @java.lang.Override public io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_GET_FEATURE,
            asyncUnaryCall(
              new MethodHandlers<
                  RouteGuideProto.Point,
                  RouteGuideProto.Feature>(
                  this, METHODID_GET_FEATURE)))
          .addMethod(
            METHOD_LIST_FEATURES,
            asyncServerStreamingCall(
              new MethodHandlers<
                  RouteGuideProto.Rectangle,
                  RouteGuideProto.Feature>(
                  this, METHODID_LIST_FEATURES)))
          .addMethod(
            METHOD_RECORD_ROUTE,
            asyncClientStreamingCall(
              new MethodHandlers<
                  RouteGuideProto.Point,
                  RouteGuideProto.RouteSummary>(
                  this, METHODID_RECORD_ROUTE)))
          .addMethod(
            METHOD_ROUTE_CHAT,
            asyncBidiStreamingCall(
              new MethodHandlers<
                  RouteGuideProto.RouteNote,
                  RouteGuideProto.RouteNote>(
                  this, METHODID_ROUTE_CHAT)))
          .build();
    }
  }

  /**
   */
  public static final class RouteStub extends io.grpc.stub.AbstractStub<RouteStub> {
    private RouteStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RouteStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RouteStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RouteStub(channel, callOptions);
    }

    /**
     */
    public void getFeature(RouteGuideProto.Point request,
        io.grpc.stub.StreamObserver<RouteGuideProto.Feature> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_GET_FEATURE, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void listFeatures(RouteGuideProto.Rectangle request,
        io.grpc.stub.StreamObserver<RouteGuideProto.Feature> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(METHOD_LIST_FEATURES, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<RouteGuideProto.Point> recordRoute(
        io.grpc.stub.StreamObserver<RouteGuideProto.RouteSummary> responseObserver) {
      return asyncClientStreamingCall(
          getChannel().newCall(METHOD_RECORD_ROUTE, getCallOptions()), responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<RouteGuideProto.RouteNote> routeChat(
        io.grpc.stub.StreamObserver<RouteGuideProto.RouteNote> responseObserver) {
      return asyncBidiStreamingCall(
          getChannel().newCall(METHOD_ROUTE_CHAT, getCallOptions()), responseObserver);
    }
  }

  /**
   */
  public static final class RouteBlockingStub extends io.grpc.stub.AbstractStub<RouteBlockingStub> {
    private RouteBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RouteBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RouteBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RouteBlockingStub(channel, callOptions);
    }

    /**
     */
    public RouteGuideProto.Feature getFeature(RouteGuideProto.Point request) {
      return blockingUnaryCall(
          getChannel(), METHOD_GET_FEATURE, getCallOptions(), request);
    }

    /**
     */
    public java.util.Iterator<RouteGuideProto.Feature> listFeatures(
        RouteGuideProto.Rectangle request) {
      return blockingServerStreamingCall(
          getChannel(), METHOD_LIST_FEATURES, getCallOptions(), request);
    }
  }

  /**
   */
  public static final class RouteFutureStub extends io.grpc.stub.AbstractStub<RouteFutureStub> {
    private RouteFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RouteFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RouteFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RouteFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<RouteGuideProto.Feature> getFeature(
        RouteGuideProto.Point request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_GET_FEATURE, getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_FEATURE = 0;
  private static final int METHODID_LIST_FEATURES = 1;
  private static final int METHODID_RECORD_ROUTE = 2;
  private static final int METHODID_ROUTE_CHAT = 3;

  private static class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final RouteImplBase serviceImpl;
    private final int methodId;

    public MethodHandlers(RouteImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_FEATURE:
          serviceImpl.getFeature((RouteGuideProto.Point) request,
              (io.grpc.stub.StreamObserver<RouteGuideProto.Feature>) responseObserver);
          break;
        case METHODID_LIST_FEATURES:
          serviceImpl.listFeatures((RouteGuideProto.Rectangle) request,
              (io.grpc.stub.StreamObserver<RouteGuideProto.Feature>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_RECORD_ROUTE:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.recordRoute(
              (io.grpc.stub.StreamObserver<RouteGuideProto.RouteSummary>) responseObserver);
        case METHODID_ROUTE_CHAT:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.routeChat(
              (io.grpc.stub.StreamObserver<RouteGuideProto.RouteNote>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    return new io.grpc.ServiceDescriptor(SERVICE_NAME,
        METHOD_GET_FEATURE,
        METHOD_LIST_FEATURES,
        METHOD_RECORD_ROUTE,
        METHOD_ROUTE_CHAT);
  }

}
