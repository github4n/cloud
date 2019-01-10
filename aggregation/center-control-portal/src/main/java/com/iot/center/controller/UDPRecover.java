package com.iot.center.controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.iot.center.helper.ResponseBodys;

@Controller
@RequestMapping("/")
public class UDPRecover {

	private String gatewayMsg = "";

	@RequestMapping("/recover")
	@ResponseBody
	public ResponseBodys recover(HttpServletRequest request) {
		String str = "";
		try {
			final DatagramSocket detectSocket = new DatagramSocket(8085);
			new Thread(() -> {
				System.out.println("Receive thread started.");
				int a = 0;
				while (true) {
					byte[] buf = new byte[1024];
					DatagramPacket packet = new DatagramPacket(buf, buf.length);
					try {
						detectSocket.receive(packet);
					} catch (IOException e) {
						e.printStackTrace();
					}
					gatewayMsg = "Received from " + packet.getSocketAddress() + ", Data="
							+ new String(packet.getData(), 0, packet.getLength());
					System.out.println(gatewayMsg);
					gatewayMsg = gatewayMsg + "->==->";
					a++;
					if (a > 100) {
						break;
					}
				}
			}).start();

			// --------------------------

			InetAddress hostAddress = InetAddress.getByName("255.255.255.255");
			byte[] buf = new byte[1024];
			int packetPort = 36877;
			buf = "leedarson_gateway".getBytes();
			System.out.println("Send to " + hostAddress);
			DatagramPacket out = new DatagramPacket(buf, buf.length, hostAddress, packetPort);
			detectSocket.send(out);
			Thread.sleep(10000L);
		} catch (SocketException e1) {
			e1.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseBodys(200, gatewayMsg);
	}
}
