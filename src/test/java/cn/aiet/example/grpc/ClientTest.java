package cn.aiet.example.grpc;

import com.google.common.util.concurrent.ListenableFuture;
import io.grpc.Status;
import io.grpc.StatusException;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.CompletableFuture.runAsync;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author aiet
 */
class ClientTest {

  private static RouteServer server;
  private static int port;
  private static Logger LOG = LoggerFactory.getLogger(ClientTest.class);
  private static RouteGrpc.RouteBlockingStub blockingStub;
  private static RouteGrpc.RouteStub stub;
  private static RouteGrpc.RouteFutureStub futureStub;

  @BeforeAll
  static void init() throws Exception {
    port = 9999;
    server = new RouteServer(port);
    runAsync(() -> {
      try {
        server.start();
      } catch (Exception e) {
        LOG.error("failed to start server", e);
      }
    });
    RouteClient client = new RouteClient(port);
    blockingStub = client.connectBlocking();
    futureStub = client.connectAsync();
    stub = client.connect();
  }

  @AfterAll
  static void destroy() throws Exception {
    server.stop();
  }

  @Test
  void getFeature() throws Exception {
    StreamObserver<RouteGuideProto.Feature> observer = mock(StreamObserver.class);
    stub.getFeature(RouteGuideProto.Point.newBuilder().setLat(1).setLng(2).build(), observer);
    /* waits for server to respond */
    assertTimeout(Duration.ofMillis(1300), () -> TimeUnit.SECONDS.sleep(1));
    verify(observer).onNext(any(RouteGuideProto.Feature.class));
    verify(observer).onNext(argThat(Objects::nonNull));
    verify(observer).onNext(argThat(feature ->
        "test".equals(feature.getName()) && feature.getLoc() != null &&
            2 == feature.getLoc().getLat() && 3 == feature.getLoc().getLng()
    ));
    verify(observer, times(1)).onCompleted();
  }

  @Test
  void listFeatures() throws Exception {
    StreamObserver<RouteGuideProto.Feature> observer = mock(StreamObserver.class);
    stub.listFeatures(RouteGuideProto.Rectangle.newBuilder()
        .setLt(RouteGuideProto.Point.newBuilder().setLat(1).setLng(2))
        .setRb(RouteGuideProto.Point.newBuilder().setLat(3).setLng(4))
        .build(), observer
    );
    /* waits for server to respond */
    assertTimeout(Duration.ofMillis(1300), () -> TimeUnit.SECONDS.sleep(1));
    Iterator<String> nameIter = Arrays.asList("test1", "test2", "test3").iterator();
    Iterator<Integer> latIter = Arrays.asList(2, 4, 8).iterator();
    Iterator<Integer> lngIter = Arrays.asList(3, 6, 9).iterator();
    verify(observer, times(3)).onNext(any(RouteGuideProto.Feature.class));
    verify(observer, times(3)).onNext(argThat(Objects::nonNull));
    verify(observer, never()).onError(null);
    ArgumentCaptor<RouteGuideProto.Feature> argumentCaptor = ArgumentCaptor.forClass(RouteGuideProto.Feature.class);
    verify(observer, times(3).description("arg value wrong")).onNext(argumentCaptor.capture());
    Iterator<RouteGuideProto.Feature> features = argumentCaptor.getAllValues().iterator();
    for (RouteGuideProto.Feature feature : argumentCaptor.getAllValues()) {
      assertAll(
          () -> assertEquals(nameIter.next(), feature.getName()),
          () -> Assertions.assertEquals(latIter.next().intValue(), feature.getLoc().getLat()),
          () -> Assertions.assertEquals(lngIter.next().intValue(), feature.getLoc().getLng())
      );
    }
    verify(observer, times(1)).onCompleted();
  }

  @Test
  void recordRouteComplete() throws Exception {
    StreamObserver<RouteGuideProto.RouteSummary> observer = mock(StreamObserver.class);
    StreamObserver<RouteGuideProto.Point> pointObserver = stub.recordRoute(observer);

    pointObserver.onNext(RouteGuideProto.Point.newBuilder().setLng(2).setLat(3).build());
    pointObserver.onNext(RouteGuideProto.Point.newBuilder().setLng(6).setLat(2).build());
    pointObserver.onNext(RouteGuideProto.Point.newBuilder().setLng(2).setLat(3).build());

    verify(observer, never()).onCompleted();

    pointObserver.onCompleted();

    /* waits for server to respond */
    assertTimeout(Duration.ofMillis(1300), () -> TimeUnit.SECONDS.sleep(1));

    ArgumentCaptor<RouteGuideProto.RouteSummary> argumentCaptor = ArgumentCaptor.forClass(RouteGuideProto.RouteSummary.class);
    verify(observer, times(1)).onNext(argumentCaptor.capture());
    RouteGuideProto.RouteSummary summary = argumentCaptor.getValue();
    assertAll(
        () -> assertEquals(8, summary.getDistance()),
        () -> assertEquals(1, summary.getFeatureCount()),
        () -> assertEquals(3, summary.getPointCount()),
        () -> assertTrue(summary.getElapse() < 2)
    );

  }

  @Test
  void recordRouteError() throws Exception {
    StreamObserver<RouteGuideProto.RouteSummary> observer = mock(StreamObserver.class);
    StreamObserver<RouteGuideProto.Point> pointObserver = stub.recordRoute(observer);

    pointObserver.onError(Status.CANCELLED.withDescription("for test").asException());
    assertThrows(IllegalStateException.class, () -> pointObserver.onNext(RouteGuideProto.Point.newBuilder().setLng(2).setLat(3).build()));
    ArgumentCaptor<Throwable> argumentCaptor = ArgumentCaptor.forClass(Throwable.class);
    verify(observer, times(1)).onError(argumentCaptor.capture());
    assertEquals(StatusException.class, argumentCaptor.getValue().getCause().getClass());
    assertEquals("CANCELLED: for test", argumentCaptor.getValue().getCause().getMessage());

    assertThrows(IllegalStateException.class, pointObserver::onCompleted);
    verify(observer, never()).onCompleted();
  }

  @Test
  void routeChat() throws Exception {
    StreamObserver<RouteGuideProto.RouteNote> observer = mock(StreamObserver.class);
    StreamObserver<RouteGuideProto.RouteNote> notes = stub.routeChat(observer);
    List<String> msgs = Arrays.asList("test1", "test2", "test3");
    List<Integer> lats = Arrays.asList(1, 2, 3);
    List<Integer> lngs = Arrays.asList(10, 9, 8);
    Iterator<String> msgIter = msgs.iterator();
    Iterator<Integer> latIter = lats.iterator();
    Iterator<Integer> lngIter = lngs.iterator();

    for (int i = 0; i < msgs.size(); i++) {
      notes.onNext(RouteGuideProto.RouteNote.newBuilder()
          .setLoc(RouteGuideProto.Point.newBuilder().setLat(latIter.next()).setLng(lngIter.next()).build())
          .setMsg(msgIter.next()).build()
      );
    }
    notes.onCompleted();

    /* waits for server to respond */
    assertTimeout(Duration.ofMillis(1300), () -> TimeUnit.SECONDS.sleep(1));

    ArgumentCaptor<RouteGuideProto.RouteNote> noteCaptor = ArgumentCaptor.forClass(RouteGuideProto.RouteNote.class);
    verify(observer, times(msgs.size())).onNext(noteCaptor.capture());
    verify(observer).onCompleted();

    List<RouteGuideProto.RouteNote> serverNotes = noteCaptor.getAllValues();
    Iterator<String> msgVerifyIter = msgs.iterator();
    Iterator<Integer> latVerifyIter = lats.iterator();
    Iterator<Integer> lngVerifyIter = lngs.iterator();
    for (RouteGuideProto.RouteNote serverNote : serverNotes) {
      assertAll(
          () -> assertEquals("from server: " + msgVerifyIter.next(), serverNote.getMsg()),
          () -> Assertions.assertEquals(latVerifyIter.next().intValue(), serverNote.getLoc().getLat()),
          () -> Assertions.assertEquals(lngVerifyIter.next().intValue(), serverNote.getLoc().getLng())
      );
    }
  }

  @Test
  void getFeatureBlocking() throws Exception {
    RouteGuideProto.Feature feature = blockingStub.getFeature(RouteGuideProto.Point.newBuilder().setLat(1).setLng(2).build());
    assertNotNull(feature, "didn't get feature");
    assertAll(
        () -> assertEquals("test", feature.getName()),
        () -> Assertions.assertEquals(2, feature.getLoc().getLat()),
        () -> Assertions.assertEquals(3, feature.getLoc().getLng())
    );
  }

  @Test
  void getFeatureAsync() throws Exception {
    ListenableFuture<RouteGuideProto.Feature> futureFeatures = futureStub.getFeature(RouteGuideProto.Point.newBuilder().setLat(1).setLng(2).build());
    futureFeatures.addListener(() -> LOG.info("feature got"), Executors.newSingleThreadExecutor());
    ThreadLocal<RouteGuideProto.Feature> featureThreadLocal = new ThreadLocal<>();
    assertTimeout(Duration.ofSeconds(1), () -> featureThreadLocal.set(futureFeatures.get(1, TimeUnit.SECONDS)));
    assertNotNull(featureThreadLocal.get());
    assertAll(
        () -> assertEquals("test", featureThreadLocal.get().getName()),
        () -> Assertions.assertEquals(2, featureThreadLocal.get().getLoc().getLat()),
        () -> Assertions.assertEquals(3, featureThreadLocal.get().getLoc().getLng())
    );
  }

  @Test
  void listFeaturesBlocking() throws Exception {
    Iterator<RouteGuideProto.Feature> featureIterator = blockingStub.listFeatures(RouteGuideProto.Rectangle.newBuilder()
        .setLt(RouteGuideProto.Point.newBuilder().setLat(1).setLng(2).build())
        .setRb(RouteGuideProto.Point.newBuilder().setLat(2).setLng(4).build())
        .build());
    assertNotNull(featureIterator);
    assertAll(
        () -> assertEquals("test1", featureIterator.next().getName()),
        () -> assertEquals("test2", featureIterator.next().getName()),
        () -> assertEquals("test3", featureIterator.next().getName()),
        () -> assertFalse(featureIterator.hasNext())
    );
  }

}
