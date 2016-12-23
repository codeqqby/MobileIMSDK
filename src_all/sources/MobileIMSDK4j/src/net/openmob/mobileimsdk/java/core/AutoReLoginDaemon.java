﻿/*
 * Copyright (C) 2016 即时通讯网(52im.net) The MobileIMSDK Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/MobileIMSDK
 *  
 * 即时通讯网(52im.net) - 即时通讯技术社区! PROPRIETARY/CONFIDENTIAL.
 * Use is subject to license terms.
 * 
 * AutoReLoginDaemon.java at 2016-2-20 11:25:57, code by Jack Jiang.
 * You can contact author with jack.jiang@52im.net or jb2011@163.com.
 */
package net.openmob.mobileimsdk.java.core;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import net.openmob.mobileimsdk.java.ClientCoreSDK;
import net.openmob.mobileimsdk.java.utils.Log;

public class AutoReLoginDaemon
{
	private static final String TAG = AutoReLoginDaemon.class.getSimpleName();

	public static int AUTO_RE$LOGIN_INTERVAL = 2000;

	private boolean autoReLoginRunning = false;

	private boolean _excuting = false;

	private Timer timer = null;

	private static AutoReLoginDaemon instance = null;

	public static AutoReLoginDaemon getInstance()
	{
		if (instance == null)
			instance = new AutoReLoginDaemon();
		return instance;
	}

	private AutoReLoginDaemon()
	{
		init();
	}

	private void init()
	{
		this.timer = new Timer(AUTO_RE$LOGIN_INTERVAL, new ActionListener()
		{
			public void actionPerformed(ActionEvent e) {
				AutoReLoginDaemon.this.run();
			}
		});
	}

	public void run() {
		if (!this._excuting)
		{
			this._excuting = true;
			if (ClientCoreSDK.DEBUG)
				Log.d(TAG, "【IMCORE】自动重新登陆线程执行中, autoReLogin?" + ClientCoreSDK.autoReLogin + "...");
			int code = -1;
			// 是否允许自动重新登陆哦
			if (ClientCoreSDK.autoReLogin)
			{
				LocalUDPSocketProvider.getInstance().closeLocalUDPSocket();
				
				// 发送重登陆请求
				code = LocalUDPDataSender.getInstance().sendLogin(
						ClientCoreSDK.getInstance().getCurrentLoginName()
						, ClientCoreSDK.getInstance().getCurrentLoginPsw()
						, ClientCoreSDK.getInstance().getCurrentLoginExtra());
			}

			if (code == 0)
			{
				LocalUDPDataReciever.getInstance().startup();
			}

			this._excuting = false;
		}
	}

	public void stop()
	{
		if (this.timer != null) {
			this.timer.stop();
		}
		this.autoReLoginRunning = false;
	}

	public void start(boolean immediately)
	{
		stop();

		if (immediately)
			this.timer.setInitialDelay(0);
		else
			this.timer.setInitialDelay(AUTO_RE$LOGIN_INTERVAL);
		this.timer.start();

		this.autoReLoginRunning = true;
	}

	public boolean isautoReLoginRunning()
	{
		return this.autoReLoginRunning;
	}
}