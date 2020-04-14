package com.ct.music;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @ClassName MusicPlayer
 * @Description TODO
 * @Author caoti
 * @Date 2020/3/19 18:00
 * @since Jdk 1.8
 **/
public class MusicPlayer extends JFrame {

    public static final String DEFAULT_MUSIC_FILE_PATH = "music/";//音乐默认存储路径

    private JLabel playingMusicLabel;//用于显示正在播放的音乐
    private JButton playButton;//播放、停止按钮
    private Icon playIcon = new ImageIcon("images/play.gif");//播放按钮的图片
    private Icon stopIcon = new ImageIcon("images/stop.gif");//停止按钮的图片
    private JList<String> musicJList;
    private AudioClip audioClip;
    private boolean playState = false;//播放状态 false代表停止状态 true代表播放状态


    public static void main(String[] args){
        new MusicPlayer();
    }

    public MusicPlayer(){
        this.setSize(400,600);//大小
        this.setLocationRelativeTo(null);//位置 屏幕中心
        this.setTitle("音乐播放器");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setContentPane(createContentPanel());
        this.setResizable(false);
        this.setVisible(true);
    }

    private Container createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(createNothPanel(),BorderLayout.NORTH);
        contentPanel.add(createSouthPanel(),BorderLayout.SOUTH);
        return contentPanel;
    }

    private Component createNothPanel() {
        JPanel nothPanel = new JPanel();
        musicJList = new JList<>();
        //musicJList.setPreferredSize(new Dimension(370,500));
        musicJList.setCellRenderer(new DefaultListCellRenderer(){
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.lightGray);
                g.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
            }
        });
        musicJList.setFixedCellHeight(50);
        musicJList.setListData(this.listMusicName());// 设置选项数据 此处为音乐列表
        musicJList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                play();
            }
        });
        JScrollPane scrollPane = new JScrollPane(){
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(370, 500);//括号内参数，可以根据需要更改
            }
        };
        scrollPane.getViewport().add(musicJList);
        nothPanel.add(scrollPane);
        return nothPanel;
    }

    private Component createSouthPanel() {
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setBorder(BorderFactory.createRaisedBevelBorder());
        southPanel.add(createPlayingMusicLabelPanel(),BorderLayout.WEST);
        southPanel.add(createPlayButtonPanel(),BorderLayout.EAST);
        return southPanel;
    }

    private Component createPlayingMusicLabelPanel() {
        JPanel panel = new JPanel();
        playingMusicLabel = new JLabel("暂无正在播放的音乐");
        playingMusicLabel.setSize(350,50);
        panel.add(playingMusicLabel);
        return panel;
    }

    private Component createPlayButtonPanel() {
        JPanel panel = new JPanel();
        playButton = new JButton(playIcon);
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (playState == false){
                    play();
                }else{
                    stop();
                }
            }
        });
        panel.add(playButton);
        return panel;
    }

    /**
     * @description 扫描指定目录music下的所有音乐
     * 暂时未加过滤器 默认扫描此目录下所有文件
     * @author caoti
     */
    public String[] listMusicName(){
        //final String  path = "music";
        File file = new File(DEFAULT_MUSIC_FILE_PATH);
        String[] fileNames = file.list();
        return fileNames;
    }

    /**
     * @description 播放音乐
     * @author caoti
     */
    public void play(){
        String musicFileName;
        //如果直接点击播放按钮 默认取列表第一个音乐播放 否则选中那个播放哪个
        if (musicJList.getSelectedValue()==null){
            if (musicJList.getModel().getElementAt(0)!=null){
                musicFileName = musicJList.getModel().getElementAt(0);
            }else{
                JOptionPane.showMessageDialog(this, "暂无音乐可播放", "温馨提示", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }else{
            musicFileName = musicJList.getSelectedValue();
        }
        this.initAudioClip(DEFAULT_MUSIC_FILE_PATH,musicFileName);
        audioClip.play();//播放
        playingMusicLabel.setText(musicFileName);//正在播放标签显示当前播放的音乐
        playButton.setIcon(stopIcon);//播放按钮变为停止按钮
        playState = true;//播放状态
    }

    /**
     * @description 停止播放
     * @author caoti
     */
    public void stop(){
        if (audioClip != null){
            audioClip.stop();
            playButton.setIcon(playIcon);//停止按钮变为播放按钮
            playState = false;//停止状态
        }else{
            JOptionPane.showMessageDialog(this, "停止播放发生异常，请重启应用", "温馨提示", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * @description 初始化音频剪辑对象
     * @author caoti
     */
    public void initAudioClip(String musicFilePath,String musicFileName){
        try {
            URL url = new URL("file:"+musicFilePath + musicFileName);// 创建资源定位符
            if (audioClip==null){
                audioClip = JApplet.newAudioClip(url);
            }
        } catch (MalformedURLException e) {
            System.out.println("不是音频文件"+e.getMessage());
        }
    }
}
