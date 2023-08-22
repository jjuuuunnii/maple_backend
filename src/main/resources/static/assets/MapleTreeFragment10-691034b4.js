const A="data:image/png;base64,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";export{A as default};
