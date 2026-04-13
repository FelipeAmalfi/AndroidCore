// @ts-check

const path = require("path");

const config = {
  title: "AndroidCore Docs",
  tagline: "AndroidCore architecture, agents, and design-system guidance",
  url: process.env.DOCUSAURUS_URL || "https://example.com",
  baseUrl: process.env.DOCUSAURUS_BASE_URL || "/",

  organizationName: "url",
  projectName: "AndroidCore",
  trailingSlash: false,

  onBrokenLinks: "throw",
  markdown: {
    hooks: {
      onBrokenMarkdownLinks: "warn"
    }
  },

  i18n: {
    defaultLocale: "en",
    locales: ["en"]
  },

  presets: [
    [
      "classic",
      {
        docs: {
          path: path.resolve(__dirname, "..", "docs"),
          routeBasePath: "/",
          sidebarPath: require.resolve("./sidebars.js"),
          editUrl: "https://github.com/url/AndroidCore/tree/main/"
        },
        blog: false,
        pages: false,
        theme: {
          customCss: require.resolve("./src/css/custom.css")
        }
      }
    ]
  ],

  themeConfig: {
    navbar: {
      title: "AndroidCore",
      items: [
        {
          type: "docSidebar",
          sidebarId: "docsSidebar",
          position: "left",
          label: "Documentation"
        }
      ]
    },
    footer: {
      style: "dark",
      links: [],
      copyright: `Copyright ${new Date().getFullYear()} AndroidCore`
    }
  }
};

module.exports = config;




