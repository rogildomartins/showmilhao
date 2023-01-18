module showmilhao {
	requires transitive javafx.controls;
	requires log4j;
	requires jlayer;
	requires java.sql;
	requires java.desktop;
	
	opens br.com.showmilhao.application to javafx.graphics, javafx.fxml;
}
