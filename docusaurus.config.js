// @ts-check
// Note: type annotations allow type checking and IDEs autocompletion

const lightCodeTheme = require("prism-react-renderer/themes/github");
const darkCodeTheme = require("prism-react-renderer/themes/dracula");

/** @type {import('@docusaurus/types').Config} */
const config = {
    title: "MacroScriptingMod",
    tagline: "Learn all about the MacroScriptingMod",
    url: "https://matix-media.github.io",
    baseUrl: "/MacroScriptingMod",
    onBrokenLinks: "throw",
    onBrokenMarkdownLinks: "warn",
    favicon: "img/favicon.ico",
    trailingSlash: true,

    // GitHub pages deployment config.
    // If you aren't using GitHub pages, you don't need these.
    organizationName: "Matix-Media", // Usually your GitHub org/user name.
    projectName: "MacroScriptingMod", // Usually your repo name.

    // Even if you don't use internalization, you can use this field to set useful
    // metadata like html lang. For example, if your site is Chinese, you may want
    // to replace "en" with "zh-Hans".
    i18n: {
        defaultLocale: "en",
        locales: ["en"],
    },

    presets: [
        [
            "classic",
            /** @type {import('@docusaurus/preset-classic').Options} */
            ({
                docs: {
                    sidebarPath: require.resolve("./sidebars.js"),
                    sidebarCollapsed: false,
                    // Please change this to your repo.
                    // Remove this to remove the "edit this page" links.
                    editUrl: "https://github.com/Matix-Media/MacroScriptingMod/tree/docs",
                },
                blog: {
                    showReadingTime: true,
                },
                theme: {
                    customCss: require.resolve("./src/css/custom.css"),
                },
            }),
        ],
    ],

    themeConfig:
        /** @type {import('@docusaurus/preset-classic').ThemeConfig} */
        ({
            colorMode: {
                respectPrefersColorScheme: true,
            },
            navbar: {
                title: "MacroScriptingMod",
                logo: {
                    alt: "MSM",
                    src: "img/logo.svg",
                },
                items: [
                    {
                        type: "doc",
                        docId: "getting-started",
                        position: "left",
                        label: "Docs",
                    },
                    { to: "/blog", label: "Blog", position: "left" },
                    {
                        href: "https://github.com/Matix-Media/MacroScriptingMod",
                        label: "GitHub",
                        position: "right",
                    },
                ],
            },
            footer: {
                style: "dark",
                links: [
                    {
                        title: "Docs",
                        items: [
                            {
                                label: "Getting Started",
                                to: "/docs/getting-started",
                            },
                            {
                                label: "Libraries",
                                to: "/docs/libraries",
                            },
                        ],
                    },
                    {
                        title: "Community",
                        items: [
                            {
                                label: "GitHub",
                                href: "https://github.com/Matix-Media/MacroScriptingMod",
                            },
                            {
                                label: "Discord",
                                href: "#",
                            },
                        ],
                    },
                    {
                        title: "More",
                        items: [
                            {
                                label: "Blog",
                                to: "/blog",
                            },
                            {
                                label: "GitHub",
                                href: "https://github.com/facebook/docusaurus",
                            },
                        ],
                    },
                ],
                copyright: `Copyright © ${new Date().getFullYear()} Matix Media, Inc.`,
            },
            prism: {
                theme: lightCodeTheme,
                darkTheme: darkCodeTheme,
                additionalLanguages: ["lua"],
            },
        }),
};

module.exports = config;
