/*
* Copyright  ©2015 by HPEC Lab，School of Software and Microelectronics，Peking University， All rights reserved.
* 版权所有 ©2015 北京大学软件与微电子学院 高性能嵌入式计算实验室
* filename：Tb_note.java
* Author：郭威
* Version：1.1
* Date：2015/09/24
* Description：This java file is to be used for showing file upload interface, not used now.
* Modify history：
* 郭威,create file,2015/09/24
*/
package hpecl.uclass.noteDAO;

/**
 * Created by guo on 2015/9/24.
 */
public class Tb_note {
    private int _id;
    private String user;
    private String name;
    private String time;
    private String content;
    
    public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}

	private int wordcount;
    
    
    public Tb_note(){
        super();
    }
    public Tb_note(int _id,String user,String name,String time,String content,int wordcount){
        super();
        this._id=_id;
        this.user=user;
        this.name=name;
        this.time=time;
        this.content=content;

        this.wordcount=wordcount;
    }

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public int getWordcount() {
        return wordcount;
    }

    public void setWordcount(int wordcount) {
        this.wordcount = wordcount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
