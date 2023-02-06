# PokeapiApp

Tabela de conteúdos
=================
<!--ts-->
   * [Sobre o projeto](#-sobre-o-projeto)
   * [Funcionalidades](#-funcionalidades)
     * [Utilizando o app](#-utilizando-o-app)
   * [Protótipo](#-prototipagem)
   * [Tecnologias](#-tecnologias)
   * [Como executar o projeto](#-como-executar-o-projeto)
     * [Pré-requisitos](#pré-requisitos)
   * [Pontos para melhoria](#-pontos-para-melhoria)
   * [Autores](#-autores)
   * [Licença](#user-content--licença)
<!--te-->

## 💻 Sobre o projeto

Projeto final para a disciplina de Dispositivos Móveis da Universidade Federal do Espírito Santo.

Desenvolvido um aplicativo para ilustrar de forma lúdica e simples uma visualização e consumo da API [PokeAPI](https://pokeapi.co/docs/v2), onde podemos verificar informações de cada pokemon como cartinhas e fazer uma batalha entre dois pokemons.

## ⚙️ Funcionalidades

O usuário pode logar com uma conta de email e setar uma senha de no mínimo 6 caracteres. (1 Activity e 2 Fragments com Navigation)

Ao logar, o usuário verá uma tela com NavigationUI que permitirá navegar entre a tela de usuário, com suas informações e o botão de logout, tela de listagem, onde pode buscar por pokemons, favoritá-los e adicioná-los para a batalha, tela de batalha para mostrar qual dos selecionados é o vencedor, e a tela de favoritos, com os pokemons favoritados pelo usuário logado. (1 Activity e 4 Fragments)

Para consumo da listagem da API, uma listagem foi montada para representar toda a lista inicial de consulta dos Pokemons.

Uma busca faz a consulta pela existência de um pokemon com o nome especificado e mostra um item de lista se existir, ou uma mensagem de 'não encotrado' caso contrário.

Ao clicar num Pokemon, uma espécie de card aparecerá contendo a foto e nome dele, os Stats e a soma dos mesmos, vindos da consulta específica de um Pokemon numa API. (1 Fragment)

Ao clicar no "+" em cada item da listagem, a foto do mesmo irá para o Snackbar contendo a batalha. Caso se arrependa de uma escolha, basta clicar no "X" na parte lateral do Pokemon que desejar remover. Ao acessar a página de batalha, aparecerá o pokemon vencedor.

Ainda na listagem de pokemons, ao clicar na estrela, esse pokemon será favoritado e anexado a conta do usuário logado.

Na tela de favoritos, é feito uma consulta ao banco Firestore e preenchido na lista. Nessa lista, o usuário verá um 'X' em cada item de lista para remover esse pokemon dos seus favoritos.

Ao abrir a tela de batalha, cada slot de pokemon permite o usuário a clicar neles para uma visualização completa das informações na mesma visualização em card da listagem citada acima

### 🔎 Utilizando o app

[![Apresentação PokeapiApp](https://img.youtube.com/vi/5qPuP3I5ySs/0.jpg)](https://www.youtube.com/watch?v=5qPuP3I5ySs "Apresentação PokeapiApp")

## 🎨 Prototipagem

Protótipo feito no [Figma](https://www.figma.com/proto/anma4Zih6ceJMTtRQPjtiw/PokeApi-app?node-id=11%3A377&scaling=scale-down&page-id=0%3A1&starting-point-node-id=11%3A377)

## 🛠 Tecnologias

- Android

- Firebase Authentication

- Cloud Firestore

- Utilitários
  - Protótipo: [Figma](https://www.figma.com/proto/anma4Zih6ceJMTtRQPjtiw/PokeApi-app?node-id=11%3A377&scaling=scale-down&page-id=0%3A1&starting-point-node-id=11%3A377)
  - API: [PokeAPI](https://pokeapi.co/docs/v2)
  - IDE: [Android Studio](https://developer.android.com/studio)

## 🚀 Como executar o projeto

### Pré-requisitos

Para começar, é preciso ter instalado as seguintes ferramentas:
 - [Git](https://git-scm.com)
 - [Android Studio](https://developer.android.com/studio) (versão utilizada: 2022.1.1)
 
 No projeto, foi utilizada a API do Android na versão 27 com o Oreo 8.1, rodando num emulador com o Pixel 4.
 
 Abra o Android Studio abra um projeto de um repositório do GitHub.

## 💡 Pontos para melhoria

- Para uma melhor performance, atentar para alguns detalhes que disparam muitas requisições.

Para sugestões de melhorias, entre em contato ou acesse a Issue do repositório.

## 🦸 Autores

Pedro Victor Santos  
[GitHub](https://github.com/pedrovic7997)
[Email](mailto:pedrovictor6974@gmail.com)

Rogério Medeiros
[Github](https://github.com/RogerioMSantos)
[Email](mailto:rogerio.rmsj@gmail.com)

## 📝 Licença

Este projeto está sob a licença [MIT](./LICENSE).
