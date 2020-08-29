# paripassu_desafio
Repositório do projeto desenvolvido para o processo seletivo da empresa Pari Passu. Se trata de uma aplicação para controle de senhas em filas bancárias.
O frontend foi implementado em React utilizando Typescript e o backend foi implementado em Spring Framework utilizando Java.

# Deploy
Para utilizar a aplicação, siga os passos abaixo:

1. Clone o repositório do backend juntamente com o arquivo PREPAREAPP.sql;
2. Execute os comandos do arquivo sql no banco de dados PostgreSQL para preparar a estrutura da aplicação;
3. No caminho backend\src\main\resources, há um arquivo config.yaml que deve ser preenchido com as informações de conexão com o banco de dados;
4. Acesse a pasta do backend via linha de comando e execute o comando "mvn package" para compilar o projeto na pasta ".\target\paripassu";
5. Copie a pasta "paripassu" gerada para o servidor de aplicação. No Tomcat, isso é feito na pasta "webapps" do diretório de instalação.
6. Inicie o servidor de aplicação e acesse a url no navegador "localhost:PORTA/paripassu", por exemplo: "localhost:8080/paripassu."

# Instruções de Utilização
A aplicação possui três páginas:
1. ACOMPANHAMENTO DE SENHAS: É a página onde é possível acompanhar as últimas 4 senhas chamadas;
2. ÁREA DO CLIENTE: É a página em que é possível gerar novas senhas;
3. ÁREA DO GERENTE: É a página onde é possível chamar as próximas senhas e reiniciar as próximas numerações geradas.

Devido a ser utilizada persistência em banco de dados, as últimas numerações não são perdidas mesmo se a aplicação for reiniciada.
