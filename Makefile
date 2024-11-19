SRC_DIR = .
OUT_DIR = out
MAIN_CLASS = Main

.PHONY: all run clean

# Compilar os arquivos Java
all:
	javac -d $(OUT_DIR) $(shell find $(SRC_DIR) -name "*.java")

# Executar a classe principal
run: all
	java -cp $(OUT_DIR) $(MAIN_CLASS)

# Limpar arquivos compilados e remover diretórios vazios
clean:
	rm -rf $(OUT_DIR)/*
	find $(OUT_DIR) -type d -empty -delete
