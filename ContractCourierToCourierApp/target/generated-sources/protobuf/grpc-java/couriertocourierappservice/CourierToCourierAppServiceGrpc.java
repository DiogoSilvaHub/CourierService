package couriertocourierappservice;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.28.1)",
    comments = "Source: ServiceCourierToCourierApp.proto")
public final class CourierToCourierAppServiceGrpc {

  private CourierToCourierAppServiceGrpc() {}

  public static final String SERVICE_NAME = "couriertocourierappservice.CourierToCourierAppService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<couriertocourierappservice.Availability,
      couriertocourierappservice.Address> getConnectMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "connect",
      requestType = couriertocourierappservice.Availability.class,
      responseType = couriertocourierappservice.Address.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<couriertocourierappservice.Availability,
      couriertocourierappservice.Address> getConnectMethod() {
    io.grpc.MethodDescriptor<couriertocourierappservice.Availability, couriertocourierappservice.Address> getConnectMethod;
    if ((getConnectMethod = CourierToCourierAppServiceGrpc.getConnectMethod) == null) {
      synchronized (CourierToCourierAppServiceGrpc.class) {
        if ((getConnectMethod = CourierToCourierAppServiceGrpc.getConnectMethod) == null) {
          CourierToCourierAppServiceGrpc.getConnectMethod = getConnectMethod =
              io.grpc.MethodDescriptor.<couriertocourierappservice.Availability, couriertocourierappservice.Address>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "connect"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  couriertocourierappservice.Availability.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  couriertocourierappservice.Address.getDefaultInstance()))
              .setSchemaDescriptor(new CourierToCourierAppServiceMethodDescriptorSupplier("connect"))
              .build();
        }
      }
    }
    return getConnectMethod;
  }

  private static volatile io.grpc.MethodDescriptor<couriertocourierappservice.Void,
      couriertocourierappservice.Void> getDisconnectMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "disconnect",
      requestType = couriertocourierappservice.Void.class,
      responseType = couriertocourierappservice.Void.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<couriertocourierappservice.Void,
      couriertocourierappservice.Void> getDisconnectMethod() {
    io.grpc.MethodDescriptor<couriertocourierappservice.Void, couriertocourierappservice.Void> getDisconnectMethod;
    if ((getDisconnectMethod = CourierToCourierAppServiceGrpc.getDisconnectMethod) == null) {
      synchronized (CourierToCourierAppServiceGrpc.class) {
        if ((getDisconnectMethod = CourierToCourierAppServiceGrpc.getDisconnectMethod) == null) {
          CourierToCourierAppServiceGrpc.getDisconnectMethod = getDisconnectMethod =
              io.grpc.MethodDescriptor.<couriertocourierappservice.Void, couriertocourierappservice.Void>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "disconnect"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  couriertocourierappservice.Void.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  couriertocourierappservice.Void.getDefaultInstance()))
              .setSchemaDescriptor(new CourierToCourierAppServiceMethodDescriptorSupplier("disconnect"))
              .build();
        }
      }
    }
    return getDisconnectMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static CourierToCourierAppServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<CourierToCourierAppServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<CourierToCourierAppServiceStub>() {
        @java.lang.Override
        public CourierToCourierAppServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new CourierToCourierAppServiceStub(channel, callOptions);
        }
      };
    return CourierToCourierAppServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static CourierToCourierAppServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<CourierToCourierAppServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<CourierToCourierAppServiceBlockingStub>() {
        @java.lang.Override
        public CourierToCourierAppServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new CourierToCourierAppServiceBlockingStub(channel, callOptions);
        }
      };
    return CourierToCourierAppServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static CourierToCourierAppServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<CourierToCourierAppServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<CourierToCourierAppServiceFutureStub>() {
        @java.lang.Override
        public CourierToCourierAppServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new CourierToCourierAppServiceFutureStub(channel, callOptions);
        }
      };
    return CourierToCourierAppServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class CourierToCourierAppServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void connect(couriertocourierappservice.Availability request,
        io.grpc.stub.StreamObserver<couriertocourierappservice.Address> responseObserver) {
      asyncUnimplementedUnaryCall(getConnectMethod(), responseObserver);
    }

    /**
     */
    public void disconnect(couriertocourierappservice.Void request,
        io.grpc.stub.StreamObserver<couriertocourierappservice.Void> responseObserver) {
      asyncUnimplementedUnaryCall(getDisconnectMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getConnectMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                couriertocourierappservice.Availability,
                couriertocourierappservice.Address>(
                  this, METHODID_CONNECT)))
          .addMethod(
            getDisconnectMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                couriertocourierappservice.Void,
                couriertocourierappservice.Void>(
                  this, METHODID_DISCONNECT)))
          .build();
    }
  }

  /**
   */
  public static final class CourierToCourierAppServiceStub extends io.grpc.stub.AbstractAsyncStub<CourierToCourierAppServiceStub> {
    private CourierToCourierAppServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CourierToCourierAppServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CourierToCourierAppServiceStub(channel, callOptions);
    }

    /**
     */
    public void connect(couriertocourierappservice.Availability request,
        io.grpc.stub.StreamObserver<couriertocourierappservice.Address> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getConnectMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void disconnect(couriertocourierappservice.Void request,
        io.grpc.stub.StreamObserver<couriertocourierappservice.Void> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getDisconnectMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class CourierToCourierAppServiceBlockingStub extends io.grpc.stub.AbstractBlockingStub<CourierToCourierAppServiceBlockingStub> {
    private CourierToCourierAppServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CourierToCourierAppServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CourierToCourierAppServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public couriertocourierappservice.Address connect(couriertocourierappservice.Availability request) {
      return blockingUnaryCall(
          getChannel(), getConnectMethod(), getCallOptions(), request);
    }

    /**
     */
    public couriertocourierappservice.Void disconnect(couriertocourierappservice.Void request) {
      return blockingUnaryCall(
          getChannel(), getDisconnectMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class CourierToCourierAppServiceFutureStub extends io.grpc.stub.AbstractFutureStub<CourierToCourierAppServiceFutureStub> {
    private CourierToCourierAppServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CourierToCourierAppServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CourierToCourierAppServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<couriertocourierappservice.Address> connect(
        couriertocourierappservice.Availability request) {
      return futureUnaryCall(
          getChannel().newCall(getConnectMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<couriertocourierappservice.Void> disconnect(
        couriertocourierappservice.Void request) {
      return futureUnaryCall(
          getChannel().newCall(getDisconnectMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_CONNECT = 0;
  private static final int METHODID_DISCONNECT = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final CourierToCourierAppServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(CourierToCourierAppServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_CONNECT:
          serviceImpl.connect((couriertocourierappservice.Availability) request,
              (io.grpc.stub.StreamObserver<couriertocourierappservice.Address>) responseObserver);
          break;
        case METHODID_DISCONNECT:
          serviceImpl.disconnect((couriertocourierappservice.Void) request,
              (io.grpc.stub.StreamObserver<couriertocourierappservice.Void>) responseObserver);
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
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class CourierToCourierAppServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    CourierToCourierAppServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return couriertocourierappservice.ServiceCourierToCourierApp.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("CourierToCourierAppService");
    }
  }

  private static final class CourierToCourierAppServiceFileDescriptorSupplier
      extends CourierToCourierAppServiceBaseDescriptorSupplier {
    CourierToCourierAppServiceFileDescriptorSupplier() {}
  }

  private static final class CourierToCourierAppServiceMethodDescriptorSupplier
      extends CourierToCourierAppServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    CourierToCourierAppServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (CourierToCourierAppServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new CourierToCourierAppServiceFileDescriptorSupplier())
              .addMethod(getConnectMethod())
              .addMethod(getDisconnectMethod())
              .build();
        }
      }
    }
    return result;
  }
}
