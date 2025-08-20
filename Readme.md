# 📚 LiterAlura - Catálogo de Livros

<div align="center">

![Java](https://img.shields.io/badge/Java-24+-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.4-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17.5-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-4+-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)

[![GitHub Stars](https://img.shields.io/github/stars/vinmartinsofc/consulta-gutendx?style=for-the-badge)](https://github.com/vinmartinsofc/consulta-gutendx/stargazers)
[![GitHub Forks](https://img.shields.io/github/forks/vinmartinsofc/consulta-gutendx?style=for-the-badge)](https://github.com/vinmartinsofc/consulta-gutendx/network)
[![License](https://img.shields.io/github/license/vinmartinsofc/consulta-gutendx?style=for-the-badge)](https://github.com/vinmartinsofc/consulta-gutendx/blob/main/LICENSE)

_Aplicação console interativa para explorar o catálogo de livros do Project Gutenberg_

[🎥 **Ver Demonstração**](https://youtu.be/xcfN6VntCak) • [📖 **Documentação**](#-funcionalidades) • [🚀 **Começar**](#-como-executar)

</div>

---

## 📋 Sobre o Projeto

**LiterAlura** é um catálogo de livros desenvolvido como parte do desafio **Oracle Next Education (ONE)**. A aplicação permite explorar mais de **70.000 livros** do Project Gutenberg através de uma interface console interativa, consumindo dados da **API Gutendx** e armazenando-os localmente para consultas eficientes.

### 🎯 Objetivos do Projeto

- ✅ Consumir dados de APIs externas (Gutendx)
- ✅ Manipular e converter dados JSON para objetos Java
- ✅ Implementar persistência de dados com JPA/Hibernate
- ✅ Criar interface console interativa e intuitiva
- ✅ Aplicar boas práticas de programação Java

---

## ⚡ Funcionalidades

| Funcionalidade             | Descrição                                   | Status |
| -------------------------- | ------------------------------------------- | ------ |
| 🔍 **Busca por Título**    | Pesquisar livros na API Gutendx por título  | ✅     |
| 📚 **Listar Livros**       | Exibir todos os livros já pesquisados       | ✅     |
| 👤 **Listar Autores**      | Mostrar autores dos livros registrados      | ✅     |
| 🕰️ **Autores por Período** | Filtrar autores vivos em determinado ano    | ✅     |
| 🌐 **Filtro por Idioma**   | Buscar livros por idioma (pt, en, fr, etc.) | ✅     |

### 🎥 Demonstração em Vídeo

[![Demo LiterAlura](https://img.youtube.com/vi/xcfN6VntCak/maxresdefault.jpg)](https://youtu.be/xcfN6VntCak)

**O que você verá no vídeo (2:45):**

- 🖥️ Navegação pelo menu interativo
- 📖 Busca e registro de livros ("Dom Casmurro")
- 🇧🇷 Filtro por idiomas (português, francês, etc.)
- 🧮 Cálculo da idade dos autores em anos específicos

---

## 🛠️ Tecnologias Utilizadas

<div align="center">

| Tecnologia             | Versão | Uso                         |
| ---------------------- | ------ | --------------------------- |
| ☕ **Java**            | 24+    | Linguagem principal         |
| 🍃 **Spring Boot**     | 3.5.4  | Framework base              |
| 🗄️ **Spring Data JPA** | -      | Persistência de dados       |
| 🐘 **PostgreSQL**      | 17.5   | Banco de dados principal    |
| 🗃️ **H2 Database**     | -      | Banco para testes           |
| � **Maven**            | 4+     | Gerenciador de dependências |
| �📦 **Jackson**        | 2.19.0 | Processamento JSON          |
| 🌐 **Gutendx API**     | -      | Fonte de dados dos livros   |

</div>

---

## 📊 Exemplo de Saída

```
=== MENU ===
1 - Buscar livro por título
2 - Listar livros registrado (última busca)
3 - Listar autores registrados
4 - Listar autores vivos em determinado ano
5 - Listar livros por idioma (ex: pt, en, fr, etc.)
0 - Sair

---- LIVRO ----
Título: Dom Casmurro
Autor: Machado de Assis
Idioma: pt
Número de downloads: 1011.0
Nascimento: 1839, Morte: 1908 (69 anos)
_______________
```

---

## 🚀 Como Executar

### 📋 Pré-requisitos

- ☕ **Java 17+** instalado
- 🗄️ **PostgreSQL** configurado (ou usar H2 para testes)
- 🔧 **Maven** instalado
- 🌐 Conexão com internet (para API Gutendx)

### 🔧 Instalação e Execução

1. **Clone o repositório**

   ```bash
   git clone https://github.com/vinmartinsofc/consulta-gutendx.git
   cd consulta-gutendx
   ```

2. **Configure o banco de dados**

   **Para PostgreSQL (Produção):**

   ```properties
   # application.properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/consulta_gutendx
   spring.datasource.username=seu_usuario
   spring.datasource.password=sua_senha
   ```

   **Para H2 (Testes - já configurado):**

   ```properties
   # Configuração atual para testes
   spring.datasource.url=jdbc:h2:mem:testdb
   ```

3. **Execute a aplicação**

   ```bash
   # Windows
   .\mvnw.cmd spring-boot:run

   # Linux/Mac
   ./mvnw spring-boot:run
   ```

4. **Navegue pelo menu interativo**
   - Use as opções numeradas para explorar as funcionalidades
   - Teste com livros como "Dom Casmurro", "O Cortiço", etc.

---

## 🏗️ Arquitetura do Projeto

```
src/main/java/br/com/gutendx/api/
├── 📁 dto/                    # Data Transfer Objects
│   ├── AuthorDTO.java         # DTO do Autor
│   ├── BookDTO.java           # DTO do Livro
│   └── GutendxResponseDto.java # DTO da resposta da API
├── 📁 model/                  # Entidades JPA
│   ├── Author.java            # Entidade Autor
│   └── Book.java              # Entidade Livro
├── 📁 repository/             # Repositórios de dados
│   ├── AuthorRepository.java  # Repositório do Autor
│   └── BookRepository.java    # Repositório do Livro
├── 📁 service/                # Serviços de negócio
│   ├── ConsumoApi.java        # Cliente HTTP para API
│   └── ConverteDados.java     # Conversor JSON
├── 📁 principal/              # Interface do usuário
│   └── Principal.java         # Menu interativo
└── GutendxApplication.java     # Classe principal
```

---

## 🎓 Aprendizados

Durante o desenvolvimento deste projeto, foram aplicados conceitos importantes:

- 🔄 **Consumo de APIs RESTful** com HttpClient
- 📄 **Mapeamento JSON** para objetos Java com Jackson
- 🗄️ **Persistência de dados** com JPA/Hibernate
- 🔗 **Relacionamentos entre entidades** (One-to-Many)
- 📊 **Derived Queries** para consultas específicas
- 🎯 **Tratamento de exceções** e validações
- 🏛️ **Padrões de arquitetura** (Repository, Service, DTO)

---

## 👤 Autor

<div align="center">

**Vinícius Uchita**

[![LinkedIn](https://img.shields.io/badge/LinkedIn-devviniuchita-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://linkedin.com/in/devviniuchita)
[![GitHub](https://img.shields.io/badge/GitHub-devviniuchita-181717?style=for-the-badge&logo=github&logoColor=white)](https://github.com/devviniuchita)
[![Email](https://img.shields.io/badge/Email-viniciusuchita@gmail.com-D14836?style=for-the-badge&logo=gmail&logoColor=white)](mailto:viniciusuchita@gmail.com)

_São Paulo - SP | Java | JavaScript | React | Node.js_

</div>

---

## 🤝 Contribuindo

Contribuições são sempre bem-vindas! Para contribuir:

1. 🍴 Faça um fork do projeto
2. 🌟 Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. 💾 Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. 📤 Push para a branch (`git push origin feature/AmazingFeature`)
5. 🔃 Abra um Pull Request

---

## 📝 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

## 🙏 Agradecimentos

- 🎓 **Oracle Next Education (ONE)** - Programa de capacitação
- 🏫 **Alura** - Plataforma de ensino e conteúdo
- 📚 **Project Gutenberg** - Biblioteca digital gratuita
- 🔗 **Gutendx API** - API de acesso aos dados dos livros

---

<div align="center">

**⭐ Se este projeto te ajudou, considere dar uma estrela!**

[![GitHub Stars](https://img.shields.io/github/stars/vinmartinsofc/consulta-gutendx?style=social)](https://github.com/vinmartinsofc/consulta-gutendx/stargazers)

_Feito com ❤️ e ☕ por [Vinícius Uchita](https://github.com/devviniuchita)_

</div>
