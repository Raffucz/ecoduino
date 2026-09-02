# ♻️ EcoDuino

Sistema desktop desenvolvido em **Java** para gerenciamento e acompanhamento de informações relacionadas à **reciclagem e sustentabilidade**.

O EcoDuino possui uma interface gráfica desenvolvida com **Java Swing**, permitindo registrar informações de reciclagem, consultar relatórios e visualizar os dados através de gráficos.

A aplicação também conta com **integração ao Firebase** para gerenciamento e armazenamento dos dados.

---

## 📋 Funcionalidades

### ♻️ Gerenciamento de Reciclagem

* Cadastro de informações relacionadas à reciclagem.
* Registro dos dados coletados pela aplicação.
* Consulta das informações cadastradas.
* Interface dedicada para gerenciamento dos registros.

### 📊 Relatórios

* Visualização de informações consolidadas.
* Geração de relatórios a partir dos dados registrados.
* Organização das informações para facilitar a análise.

### 📈 Gráficos

* Visualização gráfica dos dados de reciclagem.
* Representação visual das informações armazenadas.
* Facilita a identificação e análise dos resultados.

### ☁️ Integração com Firebase

* Comunicação com o Firebase.
* Consulta e gerenciamento dos dados armazenados.
* Possibilidade de manter as informações sincronizadas com o banco de dados.

---

## 🚀 Tecnologias Utilizadas

| Tecnologia                    | Utilização                              |
| ----------------------------- | --------------------------------------- |
| ☕ **Java**                    | Linguagem principal da aplicação        |
| 🖥️ **Java Swing**            | Desenvolvimento da interface gráfica    |
| 🔥 **Firebase**               | Armazenamento e gerenciamento dos dados |
| 📊 **Biblioteca de gráficos** | Visualização dos dados                  |
| 📦 **Maven**                  | Gerenciamento de dependências e build   |

---

## 🏗️ Arquitetura da Aplicação

O EcoDuino foi organizado separando a **interface**, o **gerenciamento dos dados** e os componentes responsáveis pela visualização das informações.

```text
                    ♻️ ECODUINO
                        │
            ┌───────────┴───────────┐
            │                       │
       🖥️ INTERFACE              ☁️ DADOS
            │                       │
       Java Swing               Firebase
            │                       │
    ┌───────┼────────┐              │
    │       │        │              │
    ▼       ▼        ▼              ▼
  🏠      ♻️       📊           💾 Banco
Inicial  Reciclagem Relatórios
             │
             ▼
          📈 Gráficos
```

---

## 🖥️ Principais Telas

### 🏠 Tela Inicial

A `TelaInicial` funciona como ponto de entrada da aplicação e permite acessar as principais funcionalidades do sistema.

A partir dela, o usuário pode navegar entre as áreas de reciclagem, relatórios e visualização dos dados.

### ♻️ Tela de Reciclagem

A `TelaReciclagem` é responsável pelas operações relacionadas aos registros de reciclagem.

Nessa tela, o usuário pode trabalhar com as informações que posteriormente serão utilizadas nos relatórios e gráficos.

### 📊 Tela de Relatórios

A `TelaRelatorios` apresenta os dados de maneira organizada, permitindo consultar informações consolidadas do sistema.

### 📈 Tela de Gráficos

A `TelaGraficos` transforma os dados registrados em representações visuais, facilitando a interpretação das informações.

A classe `DadosGrafico` auxilia no tratamento dos dados utilizados para gerar essas visualizações.

---

## ☁️ Firebase

O projeto possui uma classe específica para comunicação com o Firebase:

```text
src/ecoduino/com/br/view/FirebaseClient.java
```

O componente é responsável por estabelecer a comunicação entre a aplicação Java e os serviços do Firebase utilizados pelo projeto.

Essa integração permite que os dados utilizados pelo sistema sejam armazenados e consultados de forma centralizada.

---

## 📂 Estrutura do Projeto

```text
EcoDuino/
│
├── src/
│   └── ecoduino/
│       └── com/
│           └── br/
│               └── view/
│                   │
│                   ├── TelaInicial.java
│                   ├── TelaReciclagem.java
│                   ├── TelaRelatorios.java
│                   ├── TelaGraficos.java
│                   ├── DadosGrafico.java
│                   ├── FirebaseClient.java
│                   └── ...
│
├── pom.xml
└── README.md
```

### 📌 Principais arquivos

| Arquivo               | Responsabilidade                             |
| --------------------- | -------------------------------------------- |
| `TelaInicial.java`    | Tela inicial e navegação principal           |
| `TelaReciclagem.java` | Gerenciamento das informações de reciclagem  |
| `TelaRelatorios.java` | Exibição dos relatórios                      |
| `TelaGraficos.java`   | Visualização gráfica dos dados               |
| `DadosGrafico.java`   | Tratamento dos dados utilizados nos gráficos |
| `FirebaseClient.java` | Comunicação com o Firebase                   |
| `pom.xml`             | Configuração do Maven e dependências         |

---

## 🔄 Fluxo da Aplicação

O funcionamento principal do EcoDuino pode ser representado da seguinte forma:

```text
             👤 USUÁRIO
                  │
                  ▼
            🏠 TELA INICIAL
                  │
        ┌─────────┼─────────┐
        │         │         │
        ▼         ▼         ▼
      ♻️        📊        📈
  Reciclagem  Relatórios  Gráficos
        │         │         │
        └─────────┼─────────┘
                  │
                  ▼
             ☁️ FIREBASE
                  │
                  ▼
              💾 DADOS
```

---

## ⚙️ Requisitos

Para executar o EcoDuino, recomenda-se:

* **JDK** compatível com a versão utilizada pelo projeto;
* **Maven**;
* IDE com suporte a projetos Java, como:

  * IntelliJ IDEA;
  * Eclipse;
  * NetBeans;
* Conexão com a internet caso os recursos do Firebase sejam utilizados.

---

## 📦 Instalação

### 1. Clone o repositório

```bash
git clone https://github.com/Larissa-Mota-Silva/ecoduino.git
```

### 2. Acesse a pasta do projeto

```bash
cd ecoduino
```

### 3. Instale as dependências

Utilizando o Maven:

```bash
mvn clean install
```

### 4. Execute a aplicação

Abra o projeto na IDE de sua preferência e execute a classe responsável pela inicialização da aplicação.

> 💡 Caso o projeto utilize configurações específicas do Firebase, verifique se as credenciais e configurações necessárias estão disponíveis antes da execução.

---

## 🧩 Gerenciamento de Dependências

O projeto utiliza **Maven** para gerenciamento das bibliotecas e dependências.

As configurações estão presentes no arquivo:

```text
pom.xml
```

Isso facilita a instalação das dependências necessárias e a reprodução do ambiente de desenvolvimento.

---

## 🎨 Interface

A interface do EcoDuino foi desenvolvida utilizando **Java Swing**, priorizando uma navegação simples entre as principais funcionalidades do sistema.

A aplicação organiza suas funcionalidades em diferentes telas, permitindo que o usuário:

* ♻️ Registre e consulte informações;
* 📊 Analise os dados;
* 📈 Visualize gráficos;
* ☁️ Trabalhe com informações armazenadas no Firebase.

---

## 🎯 Objetivo do Projeto

O EcoDuino tem como objetivo utilizar a tecnologia para auxiliar no **acompanhamento e organização de informações relacionadas à reciclagem**, transformando dados registrados em informações mais fáceis de consultar e analisar.

O projeto também reúne diferentes conceitos de desenvolvimento de software, como:

* ☕ Programação em Java;
* 🖥️ Desenvolvimento de interfaces gráficas;
* 🗂️ Organização e gerenciamento de dados;
* ☁️ Integração com serviços em nuvem;
* 📊 Visualização de dados;
* 📦 Gerenciamento de dependências com Maven.

---

## 📄 Licença

Este projeto foi desenvolvido para **fins acadêmicos e educacionais**.

---

### ♻️ EcoDuino

> **Tecnologia transformando dados em consciência ambiental. 🌱**
