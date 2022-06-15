package servnio;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.io.*;
import java.net.Socket;
import java.nio.file.Path;

public class ServerApp {   //Сервер подключается к путти и принимает сообщения, однако(см. коммент ниже):

    public static void main(String[] args) /*throws IOException*/ {

        /*PrintWriter is = null;           //На рандоме попытался реализовать команды ls, is, cat: не вышло.
        BufferedReader cat = null;
        Path ls = null;
        try {
            ls = (Path) new Socket("servername", 8189);
            is = new PrintWriter(((Socket) ls).getOutputStream(), true);
            cat = new BufferedReader(new InputStreamReader(((Socket) ls).getInputStream()));
        }catch (IOException e){
            return;
        }
        is.close();
        cat.close();*/


        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        System.out.println("Сервер подключился");
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new MainHandler());
                        }
                    });
            ChannelFuture future = b.bind(8189).sync();
            future.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
