package com.example.zju_android_2019.IJKPlayer;

import tv.danmaku.ijk.media.player.IMediaPlayer;

public interface IJKPlayerListener extends IMediaPlayer.OnBufferingUpdateListener, IMediaPlayer.OnCompletionListener, IMediaPlayer.OnPreparedListener, IMediaPlayer.OnInfoListener, IMediaPlayer.OnVideoSizeChangedListener, IMediaPlayer.OnErrorListener, IMediaPlayer.OnSeekCompleteListener {
	@Override
	public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i);

	@Override
	public void onCompletion(IMediaPlayer iMediaPlayer);

	@Override
	public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1);

	@Override
	public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1);

	@Override
	public void onPrepared(IMediaPlayer iMediaPlayer);

	@Override
	public void onSeekComplete(IMediaPlayer iMediaPlayer);

	@Override
	public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i1, int i2, int i3);
}
