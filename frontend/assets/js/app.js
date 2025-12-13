const API_BASE_URL = 'http://localhost:3000/api';

document.getElementById('btn-nuevo').addEventListener('click', async () => {
    await cargarCombosProducto();
    abrirModal('modal-crear-producto');
});

// -------------------
// Cambio de pestañas
// -------------------
document.querySelectorAll('.navbar li').forEach(item => {
    item.addEventListener('click', () => {
        const vista = item.getAttribute('data-vista');

        document.querySelectorAll('.navbar li').forEach(li =>
            li.classList.remove('active')
        );
        item.classList.add('active');

        document.querySelectorAll('.vista').forEach(sec =>
            sec.classList.remove('visible')
        );
        document.getElementById(vista).classList.add('visible');

        if (vista === 'inventario') cargarInventario();
    });
});


// ======================================================================
//  INVENTARIO — CRUD COMPLETO
// ======================================================================

async function cargarInventario() {
    const tbody = document.querySelector('#tabla-inventario tbody');
    tbody.innerHTML = '<tr><td colspan="7">Cargando...</td></tr>';

    try {
        const res = await fetch(`${API_BASE_URL}/inventario`);
        const data = await res.json();

        tbody.innerHTML = '';

        data.forEach(item => {
            const tr = document.createElement('tr');

            tr.innerHTML = `
                <td>${item.nombre}</td>
                <td>${item.codigo}</td>
                <td>${item.categoria}</td>
                <td>${item.stock_total}</td>
                <td>${item.stock_disponible}</td>
                <td>${item.stock_prestado}</td>
                <td>
                    <button class="btn-primary" onclick="editarProducto(${item.id})">Editar</button>
                    <button class="btn-secondary" onclick="abrirEliminar(${item.id})">Eliminar</button>
                </td>
            `;


            tbody.appendChild(tr);
        });

    } catch (err) {
        tbody.innerHTML = '<tr><td colspan="7">Error cargando inventario</td></tr>';
    }
}

// Cargar categorías y marcas en los <select>
async function cargarCombosProducto() {
    const [resCat, resMarca, resUbi] = await Promise.all([
        fetch(`${API_BASE_URL}/inventario/categorias`),
        fetch(`${API_BASE_URL}/inventario/marcas`),
        fetch(`${API_BASE_URL}/inventario/ubicaciones`)
    ]);

    const categorias = await resCat.json();
    const marcas = await resMarca.json();
    const ubicaciones = await resUbi.json();

    llenarSelect("prod-cat", categorias, "id_categoria", "nombre");
    llenarSelect("prod-marca", marcas, "id_marca", "nombre");
    llenarSelect("prod-ubi", ubicaciones, "id_ubicacion", "nombre");
}

function llenarSelect(id, data, valueField, labelField) {
    const select = document.getElementById(id);
    select.innerHTML = `<option value="">Seleccione...</option>`;
    data.forEach(item => {
        const opt = document.createElement("option");
        opt.value = item[valueField];
        opt.textContent = item[labelField];
        select.appendChild(opt);
    });
}

//ELIMINAR ESTO
async function cargarCombosProductoEditar() {
    const [catRes, marcaRes, ubiRes] = await Promise.all([
        fetch(`${API_BASE_URL}/inventario/categorias`),
        fetch(`${API_BASE_URL}/inventario/marcas`),
        fetch(`${API_BASE_URL}/inventario/ubicaciones`)
    ]);

    llenarSelect("edit-cat", await catRes.json(), "id_categoria", "nombre");
    llenarSelect("edit-marca", await marcaRes.json(), "id_marca", "nombre");
    llenarSelect("edit-ubi", await ubiRes.json(), "id_ubicacion", "nombre");
}

async function guardarEdicion() {
    const id = document.getElementById('edit-id').value;

    const data = {
        nombre_producto: document.getElementById('edit-nombre').value,
        categoria: document.getElementById('edit-cat').value,
        marca: document.getElementById('edit-marca').value,
        ubicacion: document.getElementById('edit-ubi').value,
        stock: document.getElementById('edit-stock').value,
        estado: document.getElementById('edit-estado').value
    };

    await fetch(`${API_BASE_URL}/inventario/${id}`, {
        method: 'PUT',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(data)
    });

    cerrarModal('modal-editar-producto');
    cargarInventario();
}


// ----------------------
// Crear producto
// ----------------------
document
  .getElementById('btn-guardar-producto')
  .addEventListener('click', guardarProducto);

async function guardarProducto() {
    const cod_interno = document.getElementById('prod-cod').value.trim();
    const nombre_producto = document.getElementById('prod-nombre').value.trim();
    const categoria = document.getElementById('prod-cat').value;
    const marca = document.getElementById('prod-marca').value;
    const cantidad = parseInt(document.getElementById('prod-stock').value);
    const ubicacion = document.getElementById('prod-ubi').value;

    if (!cod_interno || !nombre_producto || !categoria || !marca || !ubicacion) {
        alert('Completa todos los campos, incluyendo stock inicial y ubicación.');
        return;
    }

    const data = {
        cod_interno,
        nombre_producto,
        categoria,
        marca,
        cantidad,
        ubicacion
    };

    const resp = await fetch(`${API_BASE_URL}/inventario`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    });

    cerrarModal('modal-crear-producto');
    cargarInventario();
}


// ----------------------
// Editar producto
// ----------------------
function abrirEditar(id, nombre, estado) {
    document.getElementById('edit-id').value = id;
    document.getElementById('edit-nombre').value = nombre;
    document.getElementById('edit-estado').value = estado;

    abrirModal('modal-editar-producto');
}

async function actualizarProducto() {
    const id = document.getElementById('edit-id').value;

    const data = {
        nombre_producto: document.getElementById('edit-nombre').value,
        estado: document.getElementById('edit-estado').value
    };

    await fetch(`${API_BASE_URL}/inventario/${id}`, {
        method: 'PUT',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(data)
    });

    cerrarModal('modal-editar-producto');
    cargarInventario();
}

async function editarProducto(id) {
    // Cargar combos
    await cargarCombosProductoEditar();

    const res = await fetch(`${API_BASE_URL}/inventario/${id}`);
    const data = await res.json();

    document.getElementById('edit-id').value = data.id;
    document.getElementById('edit-codigo').value = data.cod_interno;
    document.getElementById('edit-nombre').value = data.nombre_producto;
    document.getElementById('edit-cat').value = data.categoria;
    document.getElementById('edit-marca').value = data.marca;
    document.getElementById('edit-ubi').value = data.ubicacion;
    document.getElementById('edit-stock').value = data.stock;
    document.getElementById('edit-estado').value = data.estado;

    abrirModal('modal-editar-producto');
}

// ------------------------------
// AGREGAR NUEVA CATEGORÍA
// ------------------------------
async function guardarCategoria() {
    const nombre = document.getElementById('new-cat-name').value.trim();
    if (!nombre) return alert("Ingrese un nombre");

    const res = await fetch(`${API_BASE_URL}/inventario/categorias`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ nombre })
    });

    const data = await res.json();

    await cargarCombosProducto();
    document.getElementById('prod-cat').value = data.id;

    cerrarModal("modal-add-cat");
}

// ------------------------------
// AGREGAR NUEVA MARCA
// ------------------------------
async function guardarMarca() {
    const nombre = document.getElementById('new-marca-name').value.trim();
    if (!nombre) return alert("Ingrese un nombre");

    const res = await fetch(`${API_BASE_URL}/inventario/marcas`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ nombre })
    });

    const data = await res.json();

    await cargarCombosProducto();
    document.getElementById('prod-marca').value = data.id;

    cerrarModal("modal-add-marca");
}

// ------------------------------
// AGREGAR NUEVA UBICACIÓN
// ------------------------------
async function guardarUbicacion() {
    const nombre_sala = document.getElementById('new-ubi-sala').value.trim();
    const estante = document.getElementById('new-ubi-estante').value.trim();
    const nivel = document.getElementById('new-ubi-nivel').value;
    const descripcion = document.getElementById('new-ubi-desc').value.trim();

    if (!nombre_sala || !estante || !nivel)
        return alert("Complete todos los campos obligatorios");

    const res = await fetch(`${API_BASE_URL}/inventario/ubicaciones`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ nombre_sala, estante, nivel, descripcion })
    });

    const data = await res.json();

    await cargarCombosProducto();
    document.getElementById('prod-ubi').value = data.id;

    cerrarModal("modal-add-ubi");
}


// ----------------------
// Eliminar producto
// ----------------------
function abrirEliminar(id) {
    document.getElementById('delete-id').value = id;
    abrirModal('modal-eliminar-producto');
}

async function confirmarEliminarProducto() {
    const id = document.getElementById('delete-id').value;

    await fetch(`${API_BASE_URL}/inventario/${id}`, {
        method: 'DELETE'
    });

    cerrarModal('modal-eliminar-producto');
    cargarInventario();
}


// ======================================================================
// MODALES
// ======================================================================

function abrirModal(id) {
    document.getElementById(id).classList.remove('hidden');
}

function cerrarModal(id) {
    document.getElementById(id).classList.add('hidden');
}


// CARGA AUTOMÁTICA AL INICIO
cargarInventario();
